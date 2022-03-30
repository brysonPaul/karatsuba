import java.util.Arrays;
import java.util.Scanner;
//Bryson Paul   Poly Solution    Professor Arup Guha COP3053C   3/30/22
public class poly {
    public static void main(String[] args) {
        //Initializes a scanner and the hold which will hold the output
        Scanner sc = new Scanner(System.in);
        //assignes values to n,t1, and t2 using input
        int n = (1 << sc.nextInt());
        long[] t1 = new long[n];
        long[] t2 = new long[n];
        //Initializes the two polynomials and adds them to the new arrays
        for (int x = 0; x < n; x++) {
            t1[x] = sc.nextLong();
        }
        for (int x = 0; x < n; x++) {
            t2[x] = sc.nextLong();
        }
        //Creates the polynomials using the data provided,
        // and creates the final answer by multiplying the two together
        polynomial p1 = new polynomial(t1);
        polynomial p2 = new polynomial(t2);
        polynomial p3 = p1.mult(p2);
        //prints output
        System.out.println(p3.out());
    }
}

class polynomial {
    private int length;//length of array coeff
    private long[] coeff;//stores the coefficients of the polynomials

    // Creates a polynomial from the coefficients stored in vals.
    // The polynomial created must store exactly (1<<k) coefficients
    // for some integer k.
    public polynomial(long[] vals) {
        this.length = vals.length;
        this.coeff = vals;
    }
    //returns the coeff
    public long[] getCoeff() {
        return this.coeff;
    }
    //prints out the final polynomial values as a stringbuilder
    public StringBuilder out() {
        StringBuilder stringHold = new StringBuilder();
        for (long l : this.coeff) {
            stringHold.append(l + "\n");
        }
        return stringHold;
    }
    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the sum of this and other in a newly created poly.
    public polynomial add(polynomial other) {
        long[] vals = new long[this.length];
        for (int x = 0; x < this.length; x++) {
            vals[x] = this.coeff[x] + other.coeff[x];
        }
        return new polynomial(vals);
    }
    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the difference of this and other in a new poly.
    public polynomial sub(polynomial other) {
        long[] vals = new long[this.length];
        for (int x = 0; x < this.length; x++) {
            vals[x] = this.coeff[x] - other.coeff[x];
        }
        return new polynomial(vals);
    }
    //adds value directly into the polynomial which called it, and adds the shift to the code to make sure it works right
    public void addIn(polynomial other, int shift) {
        for (int x = shift; x < other.length + shift; x++) {
            this.coeff[x] += other.coeff[x - shift];
        }
    }
    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the product of this and other in a newly created
    // poly, using the regular nested loop algorithm, with the
    // length being the next power of 2.
    public polynomial multSlow(polynomial other) {
        long[] res = new long[this.length + other.length - 1];
        for (int i = 0; i < this.length; i++)
            for (int j = 0; j < other.length; j++)
                res[i + j] += (this.coeff[i] * other.coeff[j]);
        return new polynomial(res);
    }
    // Both this and other must be of the same size and the
    // corresponding lengths must be powers of 2.
    // Returns the product of this and other in a newly created
    // poly, using Karatsuba’s algorithm, with the
    // length being the next power of 2.
    public polynomial mult(polynomial other) {
        if (this.length < 32) {
            return multSlow(other);
        }
        polynomial thisLow = this.getLeft();
        polynomial otherLow = other.getLeft();
        polynomial thisHigh = this.getRight();
        polynomial otherHigh = other.getRight();
        // Two recursive cases for "low" half and "high" half of poly.
        polynomial lowRec = thisLow.mult(otherLow);
        polynomial highRec = thisHigh.mult(otherHigh);
        // This turns out to be the "middle" part, when we foil.
        // Notice how we use one recursive call instead of two.
        polynomial thisSum = thisHigh.add(thisLow);
        polynomial otherSum = otherHigh.add(otherLow);
        polynomial midRec = thisSum.mult(otherSum);
        midRec = midRec.sub(lowRec.add(highRec));
        // Put the result back together, shifting each sub-answer as necessary.
        polynomial res = new polynomial(new long[(this.length << 1) - 1]);
        res.addIn(lowRec, 0);
        res.addIn(highRec, this.length);
        res.addIn(midRec, (this.length / 2));
        return res;
    }
    // Returns the left half of this poly in its own poly.
    private polynomial getLeft() {
        return new polynomial(Arrays.copyOf(this.coeff, this.length / 2));
    }
    // Returns the right half of this poly in its own poly.
    private polynomial getRight() {
        return new polynomial(Arrays.copyOfRange(this.coeff, this.length / 2, this.length));
    }
}
