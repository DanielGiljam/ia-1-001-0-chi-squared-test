package com.giljam.daniel.chisquaredtest.chi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Optimized by Daniel Giljam, 2018-03-18
 */

/**
 * @author Jon Wikström
 * @since 2018
 */
public class Table {
    
    private List<Integer> values;
    
    private int rowSize;
    private int colSize;
    
    private List<Integer> rowSums;
    private List<Integer> colSums;

    private double significance;
    
    public Table(List<Integer> values, int rowSize, int colSize) {
        this.values = values;
        this.rowSize = rowSize;
        this.colSize = colSize;
        significance = significance();
    }
    
    public List<Integer> getRowSums() {
        return rowSums;
    }

    public List<Integer> getColSums() {
        return colSums;
    }

    public double getSignificance() {
        return significance;
    }
    
    private List<Integer> observedRows() {
        if (rowSums == null) {
            rowSums = new ArrayList<>();
            List<Integer> temp = new ArrayList<>();
            int element = 0;

            for (int x = 0; x < rowSize; x++) {
                temp.clear();

                for (int y = 0; y < colSize; y++) {
                    temp.add(values.get(element));
                    element++;
                }

                rowSums.add(Misc.add(temp));
            }
        }
        return rowSums;
    }
    
    private List<Integer> observedCols() {
        if (colSums == null) {
            colSums = new ArrayList<>();
            List<Integer> temp = new ArrayList<>();

            for (int x = 0; x < colSize; x++) {
                temp.clear();
                int roof = ((rowSize - 1) * colSize) + x;

                for (int y = x; y <= roof; y += colSize) {
                    temp.add(values.get(y));
                }

                colSums.add(Misc.add(temp));
            }
        }
        return colSums;
    }
    
    private List<Double> anticipated() {
        
        List<Integer> cols = observedCols();
        List<Integer> rows = observedRows();
        List<Double> anticipated = new ArrayList<>();
        int n = Misc.add(values);
        
        for (int x = 0; x < rowSize; x++) {
            for (int y = 0; y < colSize; y++) {
                double asdf = ((double)rows.get(x) * cols.get(y)) / n;
                anticipated.add(asdf);
            }
        }
        
        return anticipated;
    }
    
    private double chi() {
        double sum = 0;
        List<Double> anticipated = anticipated();
        
        for (int x = 0; x < anticipated.size(); x++) {
            double math = (Math.pow(values.get(x) - anticipated.get(x), 2))/anticipated.get(x);
            sum += math;
        }
        
        return sum;
    }
    
    public double significance() {
        
        int df = (colSize - 1) * (rowSize - 1);
        
        List<Double> list = new ArrayList<>();
        
        switch (df) {
            case 1:
                list = new ArrayList<>(Arrays.asList(0.0, 0.0, 0.001, 0.004, 0.016, 2.706, 3.841, 5.024, 6.635, 7.879));
            break;
            case 2:
                list = new ArrayList<>(Arrays.asList(0.010, 0.020, 0.051, 0.103, 0.211, 4.605, 5.991, 7.378, 9.210, 10.597));
            break;
            case 3:
                list = new ArrayList<>(Arrays.asList(0.072, 0.115, 0.216, 0.352, 0.584, 6.251, 7.815, 9.348, 11.345, 12.838));
            break;
            case 4:
                list = new ArrayList<>(Arrays.asList(0.207, 0.297, 0.484, 0.711, 1.064, 7.779, 9.488, 11.143, 13.277, 14.860));
            break;
            case 5:
                list = new ArrayList<>(Arrays.asList(0.412, 0.554, 0.831, 1.145, 1.610, 9.236, 11.070, 12.833, 15.086, 16.750));
            break;
            case 6:
                list = new ArrayList<>(Arrays.asList(0.676, 0.872, 1.237, 1.635, 2.204, 10.645, 12.592, 14.449, 16.812, 18.548));
            break;
            case 7:
                list = new ArrayList<>(Arrays.asList(0.989, 1.239, 1.690, 2.167, 2.833, 12.017, 14.067, 16.013, 18.475, 20.278));
            break;
            case 8:
                list = new ArrayList<>(Arrays.asList(1.344, 1.646, 2.180, 2.733, 3.490, 13.362, 15.507, 17.535, 20.090, 21.955));
            break;
            case 9:
                list = new ArrayList<>(Arrays.asList(1.735, 2.088, 2.700, 3.325, 4.168, 14.684, 16.919, 19.023, 21.666, 23.589));
            break;
            case 10:
                list = new ArrayList<>(Arrays.asList(2.156, 2.558, 3.247, 3.940, 4.865, 15.987, 18.307, 20.483, 23.209, 25.188));
            break;
            case 11:
                list = new ArrayList<>(Arrays.asList(2.603, 3.053, 3.816, 4.575, 5.578, 17.275, 19.675, 21.920, 24.725, 26.757));
            break;
            case 12:
                list = new ArrayList<>(Arrays.asList(3.074, 3.571, 4.404, 5.226, 6.304, 18.549, 21.026, 23.337, 26.217, 28.300));
            break;
            case 13:
                list = new ArrayList<>(Arrays.asList(3.565, 4.107, 5.009, 5.892, 7.042, 19.812, 22.362, 24.736, 27.688, 29.819));
            break;
            case 14:
                list = new ArrayList<>(Arrays.asList(4.075, 4.660, 5.629, 6.571, 7.790, 21.064, 23.685, 26.119, 29.141, 31.319));
            break;
            case 15:
                list = new ArrayList<>(Arrays.asList(4.601, 5.229, 6.262, 7.261, 8.547, 22.307, 24.996, 27.488, 30.578, 32.801));
            break;
            case 16:
                list = new ArrayList<>(Arrays.asList(5.142, 5.812, 6.908, 7.962, 9.312, 23.542, 26.296, 28.845, 32.000, 34.267));
            break;
            case 17:
                list = new ArrayList<>(Arrays.asList(5.697, 6.408, 7.564, 8.672, 10.085, 24.769, 27.587, 30.191, 33.409, 35.718));
            break;
            case 18:
                list = new ArrayList<>(Arrays.asList(6.265, 7.015, 8.231, 9.390, 10.865, 25.989, 28.869, 31.526, 34.805, 37.156));
            break;
            case 19:
                list = new ArrayList<>(Arrays.asList(6.844, 7.633, 8.907, 10.117, 11.651, 27.204, 30.144, 32.852, 36.191, 38.582));
            break;
            case 20:
                list = new ArrayList<>(Arrays.asList(7.434, 8.260, 9.591, 10.851, 12.443, 28.412, 31.410, 34.170, 37.566, 39.997));
            break;
            case 21:
                list = new ArrayList<>(Arrays.asList(8.034, 8.897, 10.283, 11.591, 13.240, 29.615, 32.671, 35.479, 38.932, 41.401));
            break;
            case 22:
                list = new ArrayList<>(Arrays.asList(8.643, 9.542, 10.982, 12.338, 14.041, 30.813, 33.924, 36.781, 40.289, 42.796));
            break;
            case 23:
                list = new ArrayList<>(Arrays.asList(9.260, 10.196, 11.689, 13.091, 14.848, 32.007, 35.172, 38.076, 41.638, 44.181));
            break;
            case 24:
                list = new ArrayList<>(Arrays.asList(9.886, 10.856, 12.401, 13.848, 15.659, 33.196, 36.415, 39.364, 42.980, 45.559));
            break;
            case 25:
                list = new ArrayList<>(Arrays.asList(10.520, 11.524, 13.120, 14.611, 16.473, 34.382, 37.652, 40.646, 44.314, 46.928));
            break;
            case 26:
                list = new ArrayList<>(Arrays.asList(11.160, 12.198, 13.844, 15.379, 17.292, 35.563, 38.885, 41.923, 45.642, 48.290));
            break;
            case 27:
                list = new ArrayList<>(Arrays.asList(11.808, 12.879, 14.573, 16.151, 18.114, 36.741, 40.113, 43.195, 46.963, 49.645));
            break;
            case 28:
                list = new ArrayList<>(Arrays.asList(12.461, 13.565, 15.308, 16.928, 18.939, 37.916, 41.337, 44.461, 48.278, 50.993));
            break;
            case 29:
                list = new ArrayList<>(Arrays.asList(13.121, 14.256, 16.047, 17.708, 19.768, 39.087, 42.557, 45.722, 49.588, 52.336));
            break;
            case 30:
                list = new ArrayList<>(Arrays.asList(13.787, 14.953, 16.791, 18.493, 20.599, 40.256, 43.773, 46.979, 50.892, 53.672));
            break;
            default:
                if (df > 30 && df < 40) {
                    list = new ArrayList<>(Arrays.asList(20.707, 22.164, 24.433, 26.509, 29.051, 51.805, 55.758, 59.342, 63.691, 66.766));
                
                } else if (df > 40 && df < 50) {
                    list = new ArrayList<>(Arrays.asList(27.991, 29.707, 32.357, 34.764, 37.689, 63.167, 67.505, 71.420, 76.154, 79.490));
                
                } else if (df > 50 && df < 60) {
                    list = new ArrayList<>(Arrays.asList(35.534, 37.485, 40.482, 43.188, 46.459, 74.397, 79.082, 83.298, 88.379, 91.952));
                
                } else if (df > 60 && df < 70) {
                    list = new ArrayList<>(Arrays.asList(43.275, 45.442, 48.758, 51.739, 55.329, 85.527, 90.531, 95.023, 100.425, 104.215));
                
                } else if (df > 70 && df < 80) {
                    list = new ArrayList<>(Arrays.asList(51.172, 53.540, 57.153, 60.391, 64.278, 96.578, 101.879, 106.629, 112.329, 116.321));
                
                } else if (df > 80 && df < 90) {
                    list = new ArrayList<>(Arrays.asList(59.196, 61.754, 65.647, 69.126, 73.291, 107.565, 113.145, 118.136, 124.116, 128.299));
                
                } else if (df > 90) {
                    list = new ArrayList<>(Arrays.asList(67.328, 70.065, 74.222, 77.929, 82.358, 118.498, 124.342, 129.561, 135.807, 140.169));
                }
            break;
        }
        
        double chi = chi();
        int position = 0;
        
        for (int x = 0; x < list.size() - 1; x++) {
            if (chi > list.get(x) && chi < list.get(x + 1)) {
                position = x;
            }
        }
        
        List<Double> percent = new ArrayList<>(Arrays.asList(0.995, 0.99, 0.975, 0.95, 0.90, 0.10, 0.05, 0.025, 0.01, 0.005));
        return percent.get(position);
    }
}
