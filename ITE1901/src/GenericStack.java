public class GenericStack<E> {

    private E [] arr;

    private int index = -1;
    private int size = 100;

    GenericStack(){ arr = (E[]) new Object[size];}
    GenericStack(int size){
        this.size=size;
        arr = (E[]) new Object[this.size]; }

    public int size(){
        return this.size;
    }

    public E peek(){ return index<=-1?null:arr[index]; }

    public void push(E o){
        if(this.index>=(this.size-1)){
            int newSize = 2*this.size;
            E [] newArr = (E[]) new Object[newSize];
            System.arraycopy(arr,0,newArr,0, this.size);
            arr=newArr;
            this.size=newSize;
        }
        arr[++index]=o;
    }

    public E pop(){
        if(index<=-1)
            return null;
        E tmpElement = arr[index--];
        arr[index+1]=null;
        return tmpElement;
    }

    public boolean isEmpty(){
        return arr[0]==null;
    }

    public int capacity(){
        return this.size;
    }

    @Override
    public String toString(){
        if(arr[0]==null){
            return "Stack: [ ]";
        }

        String str="Stack: [" +  arr[0].toString();
        for (int i=1; i<=this.index; i++){
            str += ", " + arr[i].toString();
        }
        str+="]";

        return str;
    }
}