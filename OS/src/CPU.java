import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class CPU {

    private Word[] memory;
    private int pidcount;
    private int memsize;


    // memory
    // round robin
    // pcb

    public CPU() {
        memory = new Word[32];
        pidcount = 1;
        memsize = 0;
    }

    public void allocator(File file) throws IOException {
        PCB();
        int ins = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            ins++;
            memory[memsize] = new Word("Ins" + (ins + ""), st);
            memsize++;
        }
        memsize+=2;
        memory[(memsize-ins-3)].setValue(memory[(memsize-ins-3)].getValue() + (memsize + "")); //process end boundary concatenation
        pidcount++;
    }

    public void PCB ()
    {
        int temp = memsize;
        memory[memsize] = new Word("Process Id", pidcount +""); // process id
        memsize ++;
        memory[memsize] = new Word("Process State", "Not Running"); // process state
        memsize ++;
        memory[memsize] = new Word("PC", (memsize + 2) +""); // PC
        memsize ++;
        memory[memsize] = new Word("Memory Boundaries", temp+ "" + ","); // memory boundaries
        memsize ++;
    }

    public static void main(String[] args) throws IOException {
        CPU C = new CPU();
        File file1 = new File("Program 1.txt");
        C.allocator(file1);
        File file2 = new File("Program 2.txt");
        C.allocator(file2);
        File file3 = new File("Program 3.txt");
        C.allocator(file3);

//writeFile("Test","1");
    }
}


