import java.util.ArrayList;
import java.util.Arrays;

public class Permutation {
    private int n;
    private int k;
    private boolean allowEmptySpots;

    private long count;
    private int[] studentCounts;
    private int[] pairing;
    private boolean done;

    /**
     * Initialize a permutation which will select pairings of students to spots.
     * There should be at most p pairings. No pairing should be used more than
     * once. No student should be used more than once.
     * 
     * @param n
     *            Number of students.
     * @param k
     *            Number of spots available.
     */
    public Permutation(int n, int k, boolean allowEmptyPositions) {
        this.n = n;
        this.k = k;
        this.allowEmptySpots = allowEmptyPositions;

        this.count = 0;

        this.studentCounts = new int[this.n];
        Arrays.fill(this.studentCounts, 0);
        this.pairing = new int[this.k];

        if (allowEmptyPositions) {
            Arrays.fill(this.pairing, -1);
        } else {
            Arrays.fill(this.pairing, 0);
            this.studentCounts[0] = this.pairing.length;
        }

        this.done = false;
    }

    public Permutation(int n, int k) {
        this(n, k, true);
    }

	/**
	 * Return the next permutation as a Matching object, representing a solution
	 * to the Stable Matching problem contained in data.
	 * 
	 * 
	 * @param data
	 *            The Matching object containing the problem to solve.
	 * @return The next candidate solution to the problem.
	 */
    public Matching getNextMatching(Matching data) {
        int[] pairing = getNextPairing();
        if (pairing == null) {
            return null;
        }

        Matching matching = convertPairingToMatching(data, pairing);
        return matching;
    }

    /**
     * Return a Matching (StableMarriage candidate) constructed from the int[]
     * generated by getNextPairing().
     * 
     * @param data
     *            The Matching object containing the problem to solve.
     * @param pairing
     *            The candidate solution as generated by getNextPairing.
     * @return The Matching corresponding to the provided pairing.
     */
    private Matching convertPairingToMatching(Matching data, int[] pairing) {

        int m = data.getHospitalCount();
        int n = data.getResidentCount();
        ArrayList<Integer> hospital_slots = data.getHospitalSlots();

        int pairing_index = 0;
        ArrayList<Integer> resident_matching = new ArrayList<Integer>(0);
        for (int i = 0; i < n; i++) {
            resident_matching.add(-1);
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < hospital_slots.get(i); j++) {
                if (pairing[pairing_index] != -1) {
                    resident_matching.set(pairing[pairing_index], i);
                }

                if (pairing_index == pairing.length) {
                    break;
                }

                pairing_index++;
            }
        }

        return new Matching(data, resident_matching);
    }

    /**
     * Select the next pairing in "ascending order." Each student can be
     * assigned to at most one spot, and each spot can be assigned to at most
     * one student, and there are more students than there are spots. Therefore,
     * the the length of the returned array will be k, and each spot in the
     * array will contain the index of student which will fill that spot.
     * 
     * Values for the students range from 0 to n-1, so let the counting system
     * be a base-n counting system. Each spot in the array must contain an
     * integer in [0,n-1) and when a position in the array wraps around, the
     * next highest position is incremented.
     * 
     * Keep track of the number of instances of a given student's number are
     * present in the pairing. A pairing is only valid if each student is paired
     * with a position at most 1 time.
     */
    private int[] getNextPairing() {
        if (done) {
            return null;
        }

        count++;

        do {
            incrementPairing();
        } while (!isValidPairing());

        int[] nextPairing = new int[pairing.length];
        System.arraycopy(pairing, 0, nextPairing, 0, pairing.length);
        return nextPairing;
    }

    private boolean isValidPairing() {
        for (int x : studentCounts) {
            if (x > 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Increment one position of the pairing and update arrays appropriately.
     * 
     * @param index
     *            The index of the array to increment.
     */
    private void incrementPairing(int index) {
        if (index == 0) {
            count++;
        }

        if (index >= pairing.length) {
            done = true;
            return;
        }

        removeStudent(pairing[index]);

        // update students in matching
        pairing[index]++;
        if (pairing[index] >= n) {
            if (allowEmptySpots) {
                pairing[index] = -1;
            } else {
                pairing[index] = 0;
            }

            incrementPairing(index + 1);
        }

        addStudent(pairing[index]);
    }

    private void incrementPairing() {
        incrementPairing(0);
    }

    private void updateStudent(int student, int update) {
        if (student == -1 || student >= n) {
            return;
        }

        studentCounts[student] += update;
    }

    private void removeStudent(int student) {
        updateStudent(student, -1);
    }

    private void addStudent(int student) {
        updateStudent(student, 1);
    }
}
