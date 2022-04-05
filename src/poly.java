import java.util.Arrays;
import java.util.Scanner;

//Bryson Paul   Poly Solution    Professor Arup Guha COP3053C   3/30/22
public class poly {
    private int length;//length of array coeff
    private long[] coeff;//stores the coefficients of the polynomials

    // Creates a polynomial from the coefficients stored in vals.
    // The polynomial created must store exactly (1<<k) coefficients
    // for some integer k.
    public poly(long[] vals) {
        this.length = vals.length;
        this.coeff = vals;
    }
    //main method for the program
    public static void main(String[] args) {
        //Initializes a scanner and the hold which will hold the output
        Scanner sc = new Scanner(System.in);
        //assigns values to n,t1, and t2 using input
        int n = (1 << sc.nextInt());
        long[] t1 = init(n,sc);
        long[] t2 = init(n,sc);
        //Creates the polynomials using the data provided,
        // and creates the final answer by multiplying the two together
        poly p1 = new poly(t1);
        poly p2 = new poly(t2);
        poly p3 = p1.mult(p2);
        //prints output
        System.out.print(p3.out());
    }
    //Initializes a polyomial coeff array and adds the vals to a new arr
    private static long[] init(int n,Scanner sc){
        long[] hold = new long[n];
        for (int x = 0; x < n; x++) {
            hold[x] = sc.nextLong();
        }
        return hold;
    }
    //prints out the final polynomial values as a stringbuilder
    private StringBuffer out() {
        StringBuffer stringHold = new StringBuffer();
        for (long l : this.coeff) {
            stringHold.append(l + "\n");
        }
        return stringHold;
    }
    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the sum of this and other in a newly created poly.
    public poly add(poly other) {
        long[] vals = new long[this.length];
        for (int x = 0; x < this.length; x++) {
            vals[x] = this.coeff[x] + other.coeff[x];
        }
        return new poly(vals);
    }

    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the difference of this and other in a new poly.
    public poly sub(poly other) {
        long[] vals = new long[this.length];
        for (int x = 0; x < this.length; x++) {
            vals[x] = this.coeff[x] - other.coeff[x];
        }
        return new poly(vals);
    }

    //adds value directly into the polynomial which called it, and adds the shift to the code to make sure it works right
    public void addIn(poly other, int shift) {
        for (int x = shift; x < other.length + shift; x++) {
            this.coeff[x] += other.coeff[x - shift];
        }
    }

    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the product of this and other in a newly created
    // poly, using the regular nested loop algorithm, with the
    // length being the next power of 2.
    public poly multSlow(poly other) {
        long[] res = new long[this.length + other.length - 1];
        for (int i = 0; i < this.length; i++)
            for (int j = 0; j < other.length; j++)
                res[i + j] += (this.coeff[i] * other.coeff[j]);
        return new poly(res);
    }

    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the product of this and other in a newly created
    // poly, using Karatsuba’s algorithm, with the
    // length being the next power of 2.
    public poly mult(poly other) {
        if (this.length <= 32) {
            return multSlow(other);
        }
        poly thisLow = this.getLeft();
        poly otherLow = other.getLeft();
        poly thisHigh = this.getRight();
        poly otherHigh = other.getRight();
        // Two recursive cases for "low" half and "high" half of poly.
        poly lowRec = thisLow.mult(otherLow);
        poly highRec = thisHigh.mult(otherHigh);
        // This turns out to be the "middle" part, when we foil.
        // Notice how we use one recursive call instead of two.
        poly thisSum = thisHigh.add(thisLow);
        poly otherSum = otherHigh.add(otherLow);
        poly midRec = thisSum.mult(otherSum);
        midRec = midRec.sub(lowRec.add(highRec));
        // Put the result back together, shifting each sub-answer as necessary.
        poly res = new poly(new long[(this.length << 1) - 1]);
        res.addIn(lowRec, 0);
        res.addIn(highRec, this.length);
        res.addIn(midRec, (this.length / 2));
        return res;
    }

    // Returns the left half of this poly in its own poly.
    private poly getLeft() {
        return new poly(Arrays.copyOf(this.coeff, this.length / 2));
    }

    // Returns the right half of this poly in its own poly.
    private poly getRight() {
        return new poly(Arrays.copyOfRange(this.coeff, this.length / 2, this.length));
    }
}
