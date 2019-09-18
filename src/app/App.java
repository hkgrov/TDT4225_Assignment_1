package app;

import java.util.*;

//import sun.security.util.Length;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.StandardOpenOption;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.WRITE;
import static java.nio.file.StandardOpenOption.CREATE; 



public final class App {

    private static final int BLOCKSIZE = 8192;
    private static final int gb[] = {1,2,4,8,16};
    //private static final long NBLOCKS = gb*131072; 
    private static final long NBLOCKS = 122070;
    //
    //
    

    private App() {
    }

    private void readBytes(){
        System.out.println("\nAPFS/NIO    Throughput   Time");
        System.out.println("-----------------------------");
        
        for (int j : gb){
            try {
                FileInputStream in = new FileInputStream(System.getProperty("user.dir") + "/myjavadata_" + j);
                FileChannel fc = in.getChannel();
                ByteBuffer buff = ByteBuffer.allocate(BLOCKSIZE);
                long startTime = System.nanoTime();
                for (int i=0; i<NBLOCKS; i++) {
                    int bytesRead = fc.read (buff);
                    buff.clear();
                }
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);

                String list;
                list = String.format("%1$2s GB \t %2$4d MB/S \t %3$d ms", j, j*1000/(duration/1000000000), duration/1000000);
                System.out.println(list);
            }catch(FileNotFoundException e1){ 
                System.err.println("FileError: " + e1.getMessage());
            }
            catch(IOException e2){
                System.err.println("IOError: " + e2.getMessage());
            }   
        }
    } 
    

    private void writeBytes(){
        System.out.println("APFS/NIO    Throughput   Time");
        System.out.println("-----------------------------");
        try{
            for (int j : gb){
                Path file = Paths.get(System.getProperty("user.dir"), "myjavadata_" + j);
                SeekableByteChannel out = Files.newByteChannel(file, EnumSet.of(CREATE,APPEND)); 
                ByteBuffer buff = ByteBuffer.allocate(BLOCKSIZE);

                long startTime = System.nanoTime();
                for (int i = 0; i < j*NBLOCKS; i++){
                    out.write (buff);
                    buff.flip();
                }
                long endTime = System.nanoTime();
                long duration = (endTime - startTime);

            String list;
            list = String.format("%1$2s GB \t %2$4d MB/S \t %3$d ms", j, j*1000/(duration/1000000000), duration/1000000);
            System.out.println(list);
            }
        }
        
        catch(IOException e){
            System.err.println("An error occured" + e.getMessage());
        }
        
        
        
    } 


    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */

    public static void main(String[] args) {
        App test = new App();
        //test.writeBytes();
        test.readBytes();
    }    
}
