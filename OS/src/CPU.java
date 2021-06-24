import java.io.*;
import java.util.*;

public class CPU {

    private Word[] memory;
    private int pidcount;
    private int memsize;
    private ArrayList<Integer> queue;
    private Queue<Integer> q;

    // memory
    // round robin
    // pcb

    public CPU() {
        memory = new Word[32];
        pidcount = 1;
        memsize = 0;
        q = new LinkedList<Integer>();
        this.queue = new ArrayList<Integer>();
    }




    public void assign(String x, String y) {
//        memory.put(x, y);
    }

    public int add(int x, int y) {
        x = x + y;
        return x;

    }

    public static String input() {

        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();

        return s;
    }

    public static void print(String s) {
        System.out.println(s);
    }

    public static void writeFile(String x, String y) throws IOException {
        FileWriter csvWriter = new FileWriter(x);
        csvWriter.append(y);
        csvWriter.close();
    }

    public static String readFile(String x) throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader(x));
        String y = "";
        String st = "";
        while ((st = csvReader.readLine()) != null) {
            y += st + "\n";
        }
        csvReader.close();
        return y;

    }


    public void interpreter(String st) throws IOException {


        String[] code = st.split(" ");

        switch (code[0]) {

            case "assign":

                if (code.length == 3) {
                    String x = input();
                    assign(code[1], x);
                } else {
                    if (code[2].equals("readFile")) {
//                            assign(code[1], readFile(memory.get(code[3])));
                    }

                }

                break;

            case "print":

                if (code[1].equals("Enter_file_name"))
                    print(code[1]);
                else if (code[1].equals("Enter_file_data"))
                    print(code[1]);
                else if (code[1].equals("Enter_first_number"))
                    print(code[1]);
                else if (code[1].equals("Enter_second_number"))
                    print(code[1]);
                else
//                        print(memory.get(code[1]));

                    break;

            case "writeFile":

//                    writeFile(memory.get(code[1]), memory.get(code[2]));

                break;

            case "add": //call add method
//                    String a = "" + add(Integer.parseInt(memory.get(code[1])), Integer.parseInt(memory.get(code[2])));
//                    assign(code[1], a);
                break;

            default:
                System.out.println("The Method does not exist.");


        }
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
        memsize += 2;
        memory[(memsize - ins - 3)].setValue(memory[(memsize - ins - 3)].getValue() + (memsize + "")); //process end boundary concatenation
        pidcount++;
//        if (queue.size() == 3)
//            scheduler();
    }

    public void PCB() {
        int temp = memsize;
        memory[memsize] = new Word("Process Id", pidcount + ""); // process id
        memsize++;
        memory[memsize] = new Word("Process State", "Not Running"); // process state
        memsize++;
        memory[memsize] = new Word("PC", (memsize + 2) + ""); // PC
        memsize++;
        memory[memsize] = new Word("Memory Boundaries", temp + "" + ","); // memory boundaries
        memsize++;
    }

    public void scheduler1() throws IOException {
        // loop over the queue
        // check which id the program belongs to
        // depending on the id, I need to check the number of instuctions remaining,
        // the pc value to know which instruction I will execute
        // If there are remaining instruction, put the program back to the queue, else remove the program.

        while (!q.isEmpty()) {
            int pNum = q.poll();
            int num_instructions = 0;
            if (pNum == 1) {
                String[] s = memory[3].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[2].getValue());
                num_instructions = max - pc-2;
                if (num_instructions != 0) {
                    for (int i = 0; i < 2 && num_instructions > 2; i++) {
                        interpreter(memory[pc].getValue());
                    }
                } else q.add(pNum);
            } else if (pNum == 2) {
                String[] s = memory[12].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[11].getValue());
                num_instructions = max - pc-2;
                if (num_instructions != 0) {
                    for (int i = 0; i < 2 && num_instructions > 2; i++) {
                        interpreter(memory[pc].getValue());
                    }
                } else q.add(pNum);
            } else {
                    String[] s = memory[23].getValue().split(",");
                    int max = Integer.parseInt(s[1] + "");
                    int pc = Integer.parseInt(memory[22].getValue());
                    num_instructions = max - pc-2;
                    if (num_instructions != 0) {
                        for (int i = 0; i < 2 && num_instructions > 2; i++) {
                            interpreter(memory[pc].getValue());
                        }
                    }
                    else q.add(pNum);

            }
        }


    }

    public void scheduler() {
        while (!queue.isEmpty()) {

            // 1 , 2 , 3
            // p1
            // 1 --> 0 - 8
            // pc --> index 2 - (4 - 6)
            // ms1
            // update pc
            // 4 5
            // break
            if (queue.get(0) == 1) {
                String[] memboundry = memory[3].getValue().split(",");
                int noIns = Integer.parseInt(memboundry[0]) + Integer.parseInt(memboundry[1]);
                if (noIns != 1) {
                    //     memory[3-1]
                }
            } else if (queue.get(0) == 2) {

            } else {

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

        for (int i = 0; i < C.memory.length; i++) {
            if (C.memory[i] != null) {
                System.out.println(i);
                System.out.println(C.memory[i].getKey() + " " + C.memory[i].getValue());
            } else {
                System.out.println("null index here " + i);
            }
        }


//writeFile("Test","1");
    }
}


