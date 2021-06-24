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


    // print:
    // when we create a process -> PCB
    // print the lines of code
    // print PCB before and after the scheduler to show states + include quanta
    // every time we read or write, print the name of the file and what is inside it

    public CPU() {
        memory = new Word[32];
        pidcount = 1;
        memsize = 0;
        q = new LinkedList<Integer>();
        this.queue = new ArrayList<Integer>();
    }


    public void assign(String x, String y, int index) {
//        memory.put(x, y);
        memory[index] = new Word(x, y);

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

    public void writeFile(String x, String y) throws IOException {
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


    public void interpreter(String st, int index) throws IOException {


        String[] code = st.split(" ");

        switch (code[0]) {

            case "assign":

                if (code.length == 3) {
                    String x = input();

                    if (memory[index] == null) {
                        assign(code[1], x, index);
                    } else {
                        assign(code[1], x, index + 1);
                    }
//                    assign(code[1], x, index);
//                    index++;
                } else {
                    if (code[2].equals("readFile")) { // b readfile a
                        String v = readFile(memory[index].getValue());
                        assign(code[1], readFile(memory[index].getValue()), index + 1);
                        System.out.println("Name of the file being read: " + memory[index].getValue() + ", Value read: " + v);
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
                else {
//                        print(memory.get(code[1]));
                    if (code[1].equals(memory[index].getKey()))
                        print(memory[index].getValue());
                    else
                        print(memory[index + 1].getValue());

                }
                break;

            case "writeFile":

                writeFile(memory[index].getValue(), memory[index + 1].getValue());
                System.out.println("Name of the file being written: " + memory[index].getValue() + ", Value written to the file: " + memory[index + 1].getValue());
                break;

            case "add": //call add method
                String a = "" + add(Integer.parseInt(memory[index].getValue()), Integer.parseInt(memory[index + 1].getValue()));
                assign(code[1], a, index);
                break;

            default:
                System.out.println("The Method does not exist.");


        }
    }

    public void allocator(File file) throws IOException {
        PCB();
        q.add(pidcount);
        int ins = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            ins++;
            memory[memsize] = new Word("Ins" + (ins + ""), st);
            memsize++;
        }
        memsize += 2;
        memory[(memsize - ins - 3)].setValue(memory[(memsize - ins - 3)].getValue() + (memsize - 1 + "")); //process end boundary concatenation
        print_pcb(pidcount, 0);
        pidcount++;
        if (q.size() == 3){
            System.out.println("///////////////////////////////////////////////////////////////////////");
            System.out.println();
            System.out.println();
            scheduler();}
    }

    public void PCB() {
        int temp = memsize;
        memory[memsize] = new Word("Process Id:", pidcount + ""); // process id
        memsize++;
        memory[memsize] = new Word("Process State:", "Not Running"); // process state
        memsize++;
        memory[memsize] = new Word("PC:", (memsize + 2) + ""); // PC
        memsize++;
        memory[memsize] = new Word("Memory Boundaries:", temp + "" + ","); // memory boundaries
        memsize++;
    }

    public void scheduler() throws IOException {
        // loop over the queue
        // check which id the program belongs to
        // depending on the id, I need to check the number of instuctions remaining,
        // the pc value to know which instruction I will execute
        // If there are remaining instruction, put the program back to the queue, else remove the program.

        while (!(q.isEmpty())) {
            int pNum = q.poll();
            int quanta = 0;
            int num_instructions = 0;
            if (pNum == 1) {
                memory[1].setValue("Running");
                print_pcb(pNum, quanta);
                String[] s = memory[3].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[2].getValue());
                num_instructions = max - pc - 1;
                if (num_instructions != 0) {
                    if (num_instructions == 1) {
                        quanta++;
                        System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());

                        interpreter(memory[pc].getValue(), max - 1);
                        pc++;
                        memory[2].setValue(pc + "");
                        num_instructions-=1;
                    } else {
                        for (int i = 0; i < 2 && num_instructions >= 2; i++) {
                            System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());
                            quanta++;
                            interpreter(memory[pc].getValue(), max - 1);
                            pc++;
                            memory[2].setValue(pc + "");
                        }
                        num_instructions-=2;
                    }


                }
                if (num_instructions > 0) q.add(pNum);
                memory[1].setValue("Not Running");
                print_pcb(pNum, quanta);
            } else if (pNum == 2) {

                memory[10].setValue("Running");
                print_pcb(pNum, quanta);
                String[] s = memory[12].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[11].getValue());
                num_instructions = max - pc - 1;
                System.out.println("22222222222222_"+ num_instructions);
                if (num_instructions != 0) {
                    if (num_instructions == 1) {
                        quanta++;
                        System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());

                        interpreter(memory[pc].getValue(), max - 1);
                        pc++;
                        memory[11].setValue(pc + "");
                        num_instructions-=1;
                    } else {
                        for (int i = 0; i < 2 && num_instructions >= 2; i++) {
                            System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());
                            quanta++;
                            interpreter(memory[pc].getValue(), max - 1);
                            pc++;
                            memory[11].setValue(pc + "");
                        }
                        num_instructions-=2;
                    }


                }
                if (num_instructions > 0) q.add(pNum);
                memory[10].setValue("Not Running");
                print_pcb(pNum, quanta);
            } else {
                memory[21].setValue("Running");
                print_pcb(pNum, quanta);
                String[] s = memory[23].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[22].getValue());
                num_instructions = max - pc - 1;
                if (num_instructions != 0) {
                    if (num_instructions == 1) {
                        quanta++;
                        System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());

                        interpreter(memory[pc].getValue(), max - 1);
                        pc++;
                        memory[pc].setValue(pc + "");
                        num_instructions-=1;
                    } else {
                        for (int i = 0; i < 2 && num_instructions >= 2; i++) {
                            System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());
                            quanta++;
                            interpreter(memory[pc].getValue(), max - 1);
                            pc++;
                            memory[22].setValue(pc + "");
                        }
                        num_instructions-=2;
                    }


                }
                if (num_instructions > 0) q.add(pNum);
                memory[21].setValue("Not Running");
                print_pcb(pNum, quanta);
            }
        }


    }

    private void print_pcb(int pNum, int quanta) {
        System.out.println("-------------------------------------------------------------------");
        if (quanta != 0) {
            System.out.println("quanta: " + quanta);
        }
        if (pNum == 1) {
            System.out.println(memory[0].getKey() + " " + memory[0].getValue());
            System.out.println(memory[1].getKey() + " " + memory[1].getValue());
            System.out.println(memory[2].getKey() + " " + memory[2].getValue());
            System.out.println(memory[3].getKey() + " " + memory[3].getValue());
        } else if (pNum == 2) {
            System.out.println(memory[9].getKey() + " " + memory[9].getValue());
            System.out.println(memory[10].getKey() + " " + memory[10].getValue());
            System.out.println(memory[11].getKey() + " " + memory[11].getValue());
            System.out.println(memory[12].getKey() + " " + memory[12].getValue());
        } else {
            System.out.println(memory[20].getKey() + " " + memory[20].getValue());
            System.out.println(memory[21].getKey() + " " + memory[21].getValue());
            System.out.println(memory[22].getKey() + " " + memory[22].getValue());
            System.out.println(memory[23].getKey() + " " + memory[23].getValue());
        }

        System.out.println("-------------------------------------------------------------------");
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Processes created: ");
        CPU C = new CPU();
        File file1 = new File("Program 1.txt");
        C.allocator(file1);
        File file2 = new File("Program 2.txt");
        C.allocator(file2);
        File file3 = new File("Program 3.txt");
        C.allocator(file3);

        System.out.println("All memory elements: ");
        for (int i = 0; i < C.memory.length; i++) {
            if (C.memory[i] != null) {
                System.out.println("index:" + i + " " + C.memory[i].getKey() + " " + C.memory[i].getValue());
            } else {
                System.out.println("null index here " + i);
            }
        }


//writeFile("Test","1");
    }
}


