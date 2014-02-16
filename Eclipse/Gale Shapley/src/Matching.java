import java.util.ArrayList;

/**
 * A Matching represents a candidate solution to the Stable Marriage problem. A
 * Matching may or may not be stable.
 */
public class Matching {
    /** Number of hospitals. */
    private Integer m;

    /** Number of residents. */
    private Integer n;

    /** A list containing each hospital's preference list. */
    private ArrayList<ArrayList<Integer>> hospital_preference;

    /** A list containing each resident's preference list. */
    private ArrayList<ArrayList<Integer>> resident_preference;

    /** Number of slots available in each hospital. */
    private ArrayList<Integer> hospital_slots;

    /**
     * Matching information representing the index of hospital a resident is
     * matched to, -1 if not matched.
     * 
     * An empty matching is represented by a null value for this field.
     */
    private ArrayList<Integer> resident_matching;

    public Matching(Integer m, Integer n,
            ArrayList<ArrayList<Integer>> hospital_preference,
            ArrayList<ArrayList<Integer>> resident_preference,
            ArrayList<Integer> hospital_slots) {
        this.m = m;
        this.n = n;
        this.hospital_preference = hospital_preference;
        this.resident_preference = resident_preference;
        this.hospital_slots = hospital_slots;
        this.resident_matching = null;
    }

    public Matching(Integer m, Integer n,
            ArrayList<ArrayList<Integer>> hospital_preference,
            ArrayList<ArrayList<Integer>> resident_preference,
            ArrayList<Integer> hospital_slots,
            ArrayList<Integer> resident_matching) {
        this.m = m;
        this.n = n;
        this.hospital_preference = hospital_preference;
        this.resident_preference = resident_preference;
        this.hospital_slots = hospital_slots;
        this.resident_matching = resident_matching;
    }

    /**
     * Constructs a solution to the stable marriage problem, given the problem
     * as a Matching. Take a Matching which represents the problem data with no
     * solution, and a resident_matching which solves the problem given in data.
     * 
     * @param data
     *            The given problem to solve.
     * @param resident_matching
     *            The solution to the problem.
     */
    public Matching(Matching data, ArrayList<Integer> resident_matching) {
        this(data.m, data.n, data.hospital_preference,
                data.resident_preference, data.hospital_slots,
                resident_matching);
    }

    /**
     * Creates a Matching from data which includes an empty solution.
     * 
     * @param data
     *            The Matching containing the problem to solve.
     */
    public Matching(Matching data) {
        this(data.m, data.n, data.hospital_preference,
                data.resident_preference, data.hospital_slots,
                new ArrayList<Integer>(0));
    }

    public void setResidentMatching(ArrayList<Integer> resident_matching) {
        this.resident_matching = resident_matching;
    }

    public Integer getHospitalCount() {
        return m;
    }

    public Integer getResidentCount() {
        return n;
    }

    public ArrayList<ArrayList<Integer>> getHospitalPreference() {
        return hospital_preference;
    }

    public ArrayList<ArrayList<Integer>> getResidentPreference() {
        return resident_preference;
    }

    public ArrayList<Integer> getHospitalSlots() {
        return hospital_slots;
    }

    public ArrayList<Integer> getResidentMatching() {
        return resident_matching;
    }

    public int totalHospitalSlots() {
        int slots = 0;
        for (int i = 0; i < m; i++) {
            slots += hospital_slots.get(i);
        }
        return slots;
    }
    
    public String getInputSizeString() {
        return String.format("m=%d n=%d\n", m, n);
    }
    
    public String getSolutionString() {
        if (resident_matching == null) {
            return "";
        }
        
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < resident_matching.size(); i++) {
            String str = String.format("Resident %d Hospital %d", i, resident_matching.get(i));
            s.append(str);
            if (i != resident_matching.size() - 1) {
                s.append("\n");
            }
        }
        return s.toString();
    }
    
    public String toString() {
        return getInputSizeString() + getSolutionString();
    }
}
