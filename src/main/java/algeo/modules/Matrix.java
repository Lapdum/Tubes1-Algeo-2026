package algeo.modules;

public class Matrix {
    private int rows;
    private int cols;
    private double[][] data; //untuk menyimpan input dr user

    public Matrix(int rows, int cols){
        this.rows = rows;
        this.cols = cols; //this itu artiny objek yg lagi dipake/dimiliki oleh konstruktor (public matrix)
        this.data = new double[rows][cols];
    }

    public int getRows(){
        return rows;
    }

    public int getCols(){
        return cols;
    }

    public double getValue(int row, int col){
        return data[row][col];
    }

    public void set(int row, int col, double value){
        data[row][col] = value;
    }
}
