import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Driver {
    public static String filename;
    public static boolean testBruteForce;
    public static boolean testGS;
    
    public static void main(String[] args) throws Exception {
        parseArgs(args);
        
        Matching problem = parseMatchingProblem(filename);
        testRun(problem);
    }

    private static void usage() {
        System.err.println("usage: java Driver [-g] [-b] <filename>");
        System.err.println("\t-b\tTest Brute Force implementation");
        System.err.println("\t-g\tTest Gale-Shapley implementation");
        System.exit(1);
    }
    
    public static void parseArgs(String[] args) {
        if (args.length == 0) {
            usage();
        }
        
        filename = "";
        testBruteForce = false;
        testGS = false;
        boolean flagsPresent = false;
        
        for (String s : args) {
            if(s.equals("-g")) {
                flagsPresent = true;
                testGS = true;
            } else if(s.equals("-b")) {
                flagsPresent = true;
                testBruteForce = true;
            } else if(!s.startsWith("-")) {
                filename = s;
            } else {
                System.err.printf("Unknown option: %s\n", s);
                usage();
            }
        }
        
        if(!flagsPresent) {
            testBruteForce = true;
            testGS = true;
        }
    }

    public static Matching parseMatchingProblem(String inputFile)
            throws Exception {
        int m = 0;
        int n = 0;
        ArrayList<ArrayList<Integer>> hospitalPrefs, residentPrefs;
        ArrayList<Integer> hospitalSlots;

        Scanner sc = new Scanner(new File(inputFile));
        String[] inputSizes = sc.nextLine().split(" ");

        m = Integer.parseInt(inputSizes[0]);
        n = Integer.parseInt(inputSizes[1]);
        hospitalSlots = readSlotsList(sc, m);
        hospitalPrefs = readPreferenceLists(sc, m);
        residentPrefs = readPreferenceLists(sc, n);

        Matching problem = new Matching(m, n, hospitalPrefs, residentPrefs,
                hospitalSlots);

        return problem;
    }

    private static ArrayList<Integer> readSlotsList(Scanner sc, int m) {
        ArrayList<Integer> hospitalSlots = new ArrayList<Integer>(0);

        String[] slots = sc.nextLine().split(" ");
        for (int i = 0; i < m; i++) {
            hospitalSlots.add(Integer.parseInt(slots[i]));
        }

        return hospitalSlots;
    }

    private static ArrayList<ArrayList<Integer>> readPreferenceLists(
            Scanner sc, int m) {
        ArrayList<ArrayList<Integer>> preferenceLists;
        preferenceLists = new ArrayList<ArrayList<Integer>>(0);

        for (int i = 0; i < m; i++) {
            String line = sc.nextLine();
            String[] preferences = line.split(" ");
            ArrayList<Integer> preferenceList = new ArrayList<Integer>(0);
            for (Integer j = 0; j < preferences.length; j++) {
                preferenceList.add(Integer.parseInt(preferences[j]));
            }
            preferenceLists.add(preferenceList);
        }

        return preferenceLists;
    }

    public static void testRun(Matching problem) {
        Program1 program = new Program1();
        boolean isStable;
   
        if (testGS) {
            long a_ = System.nanoTime();
            Matching GSMatching = program.stableMarriageGaleShapley(problem);
            long b_ = System.nanoTime();
            System.out.println("GS Time " + (b_-a_));
            System.out.println(GSMatching);
            isStable = program.isStableMatching(GSMatching);
            System.out.printf("%s: stable? %s\n", "Gale-Shapley", isStable);
            System.out.println();
        }

        if (testBruteForce) {
            long a_ = System.nanoTime();
            Matching BFMatching = program.stableMarriageBruteForce(problem);
            long b_ = System.nanoTime();
            System.out.println("BF Time " + (b_-a_));
            System.out.println(BFMatching);
            isStable = program.isStableMatching(BFMatching);
            System.out.printf("%s: stable? %s\n", "Brute Force", isStable);
            System.out.println();
        }
    }
}
