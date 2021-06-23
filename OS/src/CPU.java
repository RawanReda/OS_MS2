import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
public class CPU {

    private Word[] memory;
    private int pidcount;
    private int memsize;
    private ArrayList<Integer> queue;


    // memory
    // round robin
    // pcb

    public CPU() {
        memory = new Word[32];
        pidcount = 1;
        memsize = 0;
        this.queue = new ArrayList<Integer>();
    }

    public void allocator(File file) throws IOException {
        PCB();
        queue.add(pidcount);
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
        if(queue.size() == 3)
            scheduler();
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

    public void scheduler(){
        while (!queue.isEmpty()){

            // 1 , 2 , 3
            // p1
            // 1 --> 0 - 8
                // pc --> index 2 - (4 - 6)
                // ms1
                 // update pc
                // 4 5
               // break
            if(queue.get(0) == 1) {
                String[] memboundry = memory[3].getValue().split(",");
                int noIns = Integer.parseInt(memboundry[0]) + Integer.parseInt(memboundry[1]);
                if (noIns != 1) {
               //     memory[3-1]
                }
            }
            else
                if(queue.get(0) == 2){

                }
                else{

                }
               // p2
            // 2 --> 9 - 20
            // mem boun.
            // 2 --> 12 - 1 = 11
            // pc --> index (temp + 2) - (13 - 17)
            // ms1
            // update pc
            // 13 14
            // break
            // p3
            // 23 - 1 = 22 (pc)
            // 3 --> 20 - 31
            //
        }
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


