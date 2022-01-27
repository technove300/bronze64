package bronze64;

//import org.lwjgl.opengl.*;
import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;


public class LevelEditorScene extends Scene {

    private String vertexShaderSource = 
    "#version 330 core\n" + 
    "layout (location=0) in vec3 aPos;\n" + 
    "layout (location=1) in vec4 aColour;\n" + 
    "out vec4 fColour;\n" + 
    "void main(){\n" + 
    "    fColour = aColour;\n" + 
    "    gl_Position = vec4(aPos, 1.0);\n" + 
    "}";
    private String fragmentShaderSource =
    "#version 330 core\n" +
    "in vec4 fColour;\n" +
    "out vec4 colour;\n" +
    "void main(){\n" +
    "    colour = fColour;\n" +
    "}";

    private int vertexId, fragmentId, shaderProgram;

    private int vaoId, vboId, eboId;


    private float[] vertexArray = {
        //pos                   //col
        0.5f,-0.5f,0.0f,      1.0f,0.0f,0.0f,1.0f,    //bottom right
        -0.5f,0.5f,0.0f,      0.0f,1.0f,0.0f,1.0f,    //top lefty
        0.5f,0.5f,0.0f,       0.0f,0.0f,1.0f,1.0f,    //top right
        -0.5f,-0.5f,0.0f,     0.0f,1.0f,1.0f,1.0f,    //bottom right 
    };

    private int[] elementArray = {

        2, 1, 0,    //top r triag
        0, 1, 3,    //bott l triag


    };


    public LevelEditorScene() {
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
    }
    
    @Override
    public void init(){
        //vompile and link shaders
        //load and compile vertex
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        //pass the shader src code to the gpu
        glShaderSource(vertexId, vertexShaderSource);
        glCompileShader(vertexId);
        //check for comp error
        int ok = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (ok == GL_FALSE){
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH);
            System.out.println("copper 64 opengl vertex shader compile error");
            System.out.println(glGetShaderInfoLog(vertexId, len));
            assert false : "";
        }

        //load and compile vertex
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        //pass the shader src code to the gpu
        glShaderSource(fragmentId, fragmentShaderSource);
        glCompileShader(fragmentId);
        //check for comp error
        ok = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (ok == GL_FALSE){
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH);
            System.out.println("copper 64 opengl fragment shader compile error");
            System.out.println(glGetShaderInfoLog(fragmentId, len));
            assert false : "";
        }

        //link shaders and check errors!

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);
        ok = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (ok == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("copper 64 opengl shader link error");
            System.out.println(glGetProgramInfoLog(shaderProgram, len));
            assert false : "";
        }
        
        // gen the vao the vbo and the ebo buffer obj and send to gpu;

        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        FloatBuffer vertexbuffer = BufferUtils.createFloatBuffer(vertexArray.length); // create a float buffer of vertic
        vertexbuffer.put(vertexArray).flip();
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexbuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colourSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colourSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);


    }

    @Override
    public void update(float dt) {
        //bind shader prog
        glUseProgram(shaderProgram);
        //bind vao
        glBindVertexArray(vaoId);
        //enable vertex atrib pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        glUseProgram(0);

    }

}
