import java.io.*;
import java.util.*;

public class CPU {
//
    static class PCB{
        int id;
        int state;
        int PC;
        int max;
        int min;

    }
    private Word[] memory;
    private int pidcount;
    private int memsize;
    private ArrayList<Integer> queue;
    private Queue<PCB> q;

    // print::
    // when we create a process -> PCB
    // print the lines of code
    // print PCB before and after the scheduler to show states + include quanta
    // every time we read or write, print the name of the file and what is inside it

    public CPU() {
        memory = new Word[32];
        pidcount = 1;
        memsize = 0;
        q = new LinkedList<PCB>();
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
        int start_index_of_pcb= memsize;
        memsize+=4;
        int ins = 0;
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            ins++;
            memory[memsize] = new Word("Ins" + (ins + ""), st);
            System.out.println(memory[memsize].getValue());
            memsize++;
        }
        memsize += 2;

        int max_boundary= memsize-1;
        PCB(start_index_of_pcb, max_boundary);
        //process end boundary concatenation

        pidcount++;
        if (q.size() == 3){
            System.out.println("///////////////////////////////////////////////////////////////////////");
            System.out.println();
            scheduler();}
    }

    public void PCB(int temp, int max_boundary) {
        PCB process= new PCB();
        memory[temp] = new Word("Process Id:", pidcount + ""); // process id
        process.id= temp;
        temp++;
        memory[temp] = new Word("Process State:", "Not Running"); // process state
        process.state= temp;
        temp++;
        memory[temp] = new Word("PC:", (temp + 2) + ""); // PC
        process.PC= temp;
        temp++;
        memory[temp] = new Word("Memory Boundaries:", temp + "" + ","+ (max_boundary + "")); // memory boundaries
        process.min= temp;
        process.max= temp;

        System.out.println("PCB info: "+ process.id+" "+ process.state+" "+ process.PC+" "+process.min);
        print_pcb(process, 0);
        q.add(process);

    }

    public void scheduler() throws IOException {
        // loop over the queue
        // check which id the program belongs to
        // depending on the id, I need to check the number of instuctions remaining,
        // the pc value to know which instruction I will execute
        // If there are remaining instruction, put the program back to the queue, else remove the program.
        int quanta_fixed=3;
        while (!(q.isEmpty())) {
            PCB pNum = q.poll();
            int quanta = 0;
            int num_instructions = 0;
            if (pNum.id == 1) {
                memory[pNum.state].setValue("Running");
                print_pcb(pNum, quanta);
                String[] s = memory[pNum.max].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[pNum.PC].getValue());
                num_instructions = max - pc - 1;
                if (num_instructions != 0) {
                    if (num_instructions == 1) {
                        quanta++;
                        System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());

                        interpreter(memory[pc].getValue(), max - 1);
                        pc++;
                        memory[pNum.PC].setValue(pc + "");
                        num_instructions-=1;
                    } else {
                        for (int i = 0; i < quanta_fixed && num_instructions >= quanta_fixed; i++) {
                            System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());
                            quanta++;
                            interpreter(memory[pc].getValue(), max - 1);
                            pc++;
                            memory[pNum.PC].setValue(pc + "");
                        }
                        num_instructions-=quanta_fixed;
                    }


                }
                if (num_instructions > 0) q.add(pNum);
                memory[pNum.state].setValue("Not Running");
                print_pcb(pNum, quanta);
            } else if (pNum.id == 2) {

                memory[pNum.state].setValue("Running");
                print_pcb(pNum, quanta);
                String[] s = memory[pNum.max].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[pNum.PC].getValue());
                num_instructions = max - pc - 1;

                if (num_instructions != 0) {
                    if (num_instructions == 1) {
                        quanta++;
                        System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());

                        interpreter(memory[pc].getValue(), max - 1);
                        pc++;
                        memory[pNum.PC].setValue(pc + "");
                        num_instructions-=1;
                    } else {
                        for (int i = 0; i < quanta_fixed && num_instructions >= quanta_fixed; i++) {
                            System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());
                            quanta++;
                            interpreter(memory[pc].getValue(), max - 1);
                            pc++;
                            memory[pNum.PC].setValue(pc + "");
                        }
                        num_instructions-=quanta_fixed;
                    }


                }
                if (num_instructions > 0) q.add(pNum);
                memory[pNum.state].setValue("Not Running");
                print_pcb(pNum, quanta);
            } else {
                memory[pNum.state].setValue("Running");
                print_pcb(pNum, quanta);
                String[] s = memory[pNum.min].getValue().split(",");
                int max = Integer.parseInt(s[1] + "");
                int pc = Integer.parseInt(memory[pNum.PC].getValue());
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
                        for (int i = 0; i < quanta_fixed && num_instructions >= quanta_fixed; i++) {
                            System.out.println(memory[pc].getKey() + " " + memory[pc].getValue());
                            quanta++;
                            interpreter(memory[pc].getValue(), max - 1);
                            pc++;
                            memory[pNum.PC].setValue(pc + "");
                        }
                        num_instructions-=quanta_fixed;
                    }


                }
                if (num_instructions > 0) q.add(pNum);
                memory[pNum.state].setValue("Not Running");
                print_pcb(pNum, quanta);
            }
        }
    }

    private void print_pcb(PCB pNum, int quanta) {
        System.out.println("-------------------------------------------------------------------");
        if (quanta != 0) {
            System.out.println("quanta: " + quanta);
        }

            System.out.println(memory[pNum.id].getKey() + " " + memory[pNum.id].getValue());
            System.out.println(memory[pNum.state].getKey() + " " + memory[pNum.state].getValue());
            System.out.println(memory[pNum.PC].getKey() + " " + memory[pNum.PC].getValue());
            System.out.println(memory[pNum.min].getKey() + " " + memory[pNum.min].getValue());


        System.out.println("-------------------------------------------------------------------");
    }

    public static void main(String[] args) throws IOException {
//        System.out.println("Processes created: ");
        CPU C = new CPU();
        File file1 = new File("Program 1.txt");
        C.allocator(file1);
        File file3 = new File("Program 3.txt");
        C.allocator(file3);
        File file2 = new File("Program 2.txt");
        C.allocator(file2);


        System.out.println("All memory elements: ");
        for (int i = 0; i < C.memory.length; i++) {
            if (C.memory[i] != null) {
                System.out.println("index:" + i + " " + C.memory[i].getKey() + " " + C.memory[i].getValue());
            } else {
                System.out.println("null index here " + i);
            }
        }

    }
}


