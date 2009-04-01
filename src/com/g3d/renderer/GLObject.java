package com.g3d.renderer;

/**
 * Describes a GL object. An encapsulation of a certain object 
 * on the native side of the graphics library.
 */
public abstract class GLObject {

    /**
     * The ID of the object, usually depends on its type.
     * Typically returned from calls like glGenTextures, glGenBuffers, etc.
     */
    protected int id = -1;

    /**
     * A reference to a "handle". By hard referencing a certain object, it's
     * possible to find when a certain GLObject is no longer used, and to delete
     * its data from the graphics library side.
     */
    protected Object handleRef = null;

    /**
     * True if the data represented by this GLObject has been changed
     * and needs to be updated before used.
     */
    protected boolean updateNeeded = true;

    /**
     * The type of the GLObject, usually specified by a subclass.
     */
    protected final Type type;

    public static enum Type {
        /**
         * A texture is an image that is applied to geometry.
         */
        Texture,

        /**
         * Vertex buffers are used to describe geometry data and it's attributes.
         */
        VertexBuffer,

        /**
         * ShaderSource is a shader source code that controls the output of
         * a certain rendering pipeline, like vertex position or fragment color.
         */
        ShaderSource,

        /**
         * A Shader is an aggregation of ShaderSources, collectively
         * they cooperate to control the vertex and fragment processor.
         */
        Shader,
    }

    public GLObject(Type type){
        this.type = type;
    }

    /**
     * Sets the ID of the GLObject. This method is used in Renderer and must
     * not be called by the user.
     * @param id The ID to set
     */
    public void setId(int id){
        if (this.id != -1)
            throw new IllegalStateException("ID has already been set for this GL object.");

        this.id = id;
    }

    /**
     * @return The ID of the object. Should not be used by user code in most
     * cases.
     */
    public int getId(){
        return id;
    }

    public void setUpdateNeeded(){
        updateNeeded = true;
    }

    /**
     * 
     */
    public void clearUpdateNeeded(){
        updateNeeded = false;
    }

    public boolean isUpdateNeeded(){
        return updateNeeded;
    }

    @Override
    public String toString(){
        return type.name() + " " + Integer.toHexString(hashCode());
    }

//    @Override
//    public boolean equals(Object other){
//        if (this == other)
//            return true;
//        if (!(other instanceof GLObject))
//            return false;
//
//    }

    // Specialized calls to be used by object manager only.

    /**
     * Called when the GL context is restarted to reset all IDs. Prevents
     * "white textures" on display restart.
     */
    public abstract void resetObject();

    /**
     * Deletes the GL object from the GPU when it is no longer used. Called
     * automatically by the GL object manager.
     * @param r
     */
    public abstract void deleteObject(Renderer r);
}
