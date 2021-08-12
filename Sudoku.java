package sample;

public class Sudoku {
    //saves solved sudoku
    private int[][] entries;

    public Sudoku(int[] entries) {
        //convert one dimensional input into sudoku-matrix
        this.entries = new int[9][9];
        for (int i=0; i<entries.length; i++) {
            this.entries[i%9][i/9] = entries[i];
        }
    }

    public int[][] getEntries() {
        return entries;
    }

    private static int[] sudokuToList(int[][] sudoku) {
        int[] list = new int[81];
        for (int i=0; i<81; i++) {
            list[i] = sudoku[i%9][i/9];
        }
        return list;
    }

    //determine whether the sudoku is still the same or not. If not, solve new Sudoku
    public static int[] sudokuSolve(int[] input, Sudoku currentSudoku) {
        for (int i=0; i<input.length; i++) {
            if ((currentSudoku.getEntries()[i%9][i/9] != input[i]) && (input[i] != 0)) {
                //solve new sudoku
                currentSudoku = new Sudoku(input);
                currentSudoku.solve();
                break;
            }
        }
        //return sudoku as list
        return sudokuToList(currentSudoku.getEntries());
    }

    public static int sudokuHint(int[] input, Sudoku currentSudoku) {
        int pos;
        for (int i=0; i<input.length; i++) {
            pos = 0;
            //load new sudoku if changes have been made
            if ((currentSudoku.getEntries()[i%9][i/9] != input[i]) && (input[i] != 0)) {
                currentSudoku = new Sudoku(input);
            }
            //if field is empty check for possible values
            if (input[i] == 0) {
                for (int j=1; j<10; j++) {
                    System.out.println(i%9 + " " + i/9);
                    if (currentSudoku.possible(i%9,i/9,j)) pos++;
                }
            }
            //if only one value possible return position of the field
            if (pos == 1) {
                return i;
            }
        }
        return -1;
    }

    //checks whether certain number can be placed in this position or not
    private boolean possible(int y, int x, int n) {
        //check if number exists in same row
        for (int i=0; i<9; i++) {
            if (entries[y][i] == n) return false;
        }
        //check if number exists in same col
        for (int i=0; i<9; i++) {
            if (entries[i][x] == n) return false;
        }
        //get position of [x,y]'s block
        int blockX = (x/3)*3;
        int blockY = (y/3)*3;

        //check whether certain number already exists in block
        for (int i = 0; i<3; i++) {
            for (int j = 0; j<3; j++) {
                if ((entries[blockY+i][blockX+j]) == n) return false;
            }
        }
        //entry would not cause any conflicts in this position
        return true;
    }

    //solving assumes only one solution is possible since that's the norm for sudokus
    public boolean solve() {
        for (int y=0; y<9; y++) {
            for (int x=0; x<9; x++) {
                //0 == empty spot
                if (entries[y][x] == 0) {
                    for (int n=1; n<10; n++) {
                        if (possible(y,x,n)) {
                            entries[y][x] = n;
                            if (solve()) return true;
                            entries[y][x] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
}
