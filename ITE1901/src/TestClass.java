import java.io.File;
import java.util.Arrays;

public class TestClass {
    public static void main(String[] args){

        File file = new File("/Users/Unicron/Desktop/abc.txt");
        System.out.println(file.length());

        //<editor-fold desc="IceCreamParlor">
        int[] arr = {5,2,4,1,3};
        int m = 7;

        int[] res = icecreamParlor(m, arr);
        System.out.println(Arrays.toString(res));
        //</editor-fold>
    }


    // Complete the icecreamParlor function below.
    static int[] icecreamParlor(int m, int[] arr) {
        int[] result = {-1,-1}; // initialize result array
        for(int i=0; i<arr.length; i++){// outer loop, to compare against
            for(int j=i+1; j<arr.length; j++){ // inner loop, to compare with
                if(arr[i]+arr[j]==m){  // Do we have a valid match?
                    if(i<=j){ // Check which should go where
                        result[0]=i+1; // compensate for 1-based indexing
                        result[1]=j+1; // because some people just want to watch the world burn...
                    } else {
                        result[0]=j+1; // compensate for 1-based indexing
                        result[1]=i+1; // because some people just want to watch the world burn...
                    }
                    return result;     // found a valid result, lets return now.
                }
            }
        }
        return result; // we probably didn't find a result, lets return {-1, -1}
    }
}
