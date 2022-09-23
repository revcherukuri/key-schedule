public class KeySchedule {

    public static void main (String [] args) {
        KeyUtils k = new KeyUtils();
        int [] key = k.getKey();
        int [] pc1 = k.getPc1();

        //perform pc1 table permutation on 64 bit key to get 56 bit key
        key = simplePermutation(key, pc1);

        int [] pc2 = k.getPc2();

        int [] left = new int [(key.length / 2)];
        int [] right = new int [(key.length / 2)];

        //split 56 bit key into left and right halves
        for (int i = 0; i < key.length; i++) {
            if (i < 28) {
                left[i] = key[i];
            }
            else {
                right[i-28] = key[i];
            }
        }


        subKey(left, right, pc2);
    }

    public static void subKey (int [] left, int [] right, int [] pc) {
        //number of left shifts is dependant on index
        int [] shifts = {1, 1, 2, 2, 2, 2, 2, 2,
                1, 2, 2, 2, 2, 2, 2, 1};

        int tempL;
        int tempR;
        for (int i = 0; i < 16; i++) {

//            shift bits
            for (int l = 0; l < shifts[i]; l++) {
                tempL = left[0];
                tempR = right[0];
                for (int j = 0; j < left.length; j++) {
                    if (j == left.length - 1) {
                        left[j] = tempL;
                        right[j] = tempR;
                    }
                    else {
                        left[j] = left[j + 1];
                        right[j] = right[j + 1];
                    }
                }
            }

//            concatenate left and right halves after shift
            int [] combined = new int[left.length + right.length];
            for (int h = 0; h < combined.length; h++) {
                if (h < 28) {
                    combined[h] = left[h];
                }
                else {
                    combined[h] = right[h-28];
                }
            }

//            permute concatenated keys via pc2 table. Trims original 56 bit key to 48 bit subkey
            int [] permuted = new int[pc.length];
            System.out.print("K"+ (i+1) + ". ");
            for (int p = 0; p < permuted.length; p++) {
                permuted[p] = combined [pc[p]-1];
                System.out.print(permuted[p] + " ");
                if (p == 5) {
                    System.out.print("- ");
                }
                else if (p > 0 && (p+1) % 6 == 0 && p < permuted.length - 1) {
                    System.out.print("- ");
                }
            }
            System.out.println();

        }
    }

    public static int[] simplePermutation(int [] key, int [] pc1) {
        int [] key56 = new int[56];
        for (int i = 0; i < pc1.length; i++) {
            key56[i] = key[pc1[i]-1];
        }
        return key56;
    }
}
