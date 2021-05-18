import java.util.ArrayDeque;
import java.util.Deque;

import static java.util.Objects.isNull;

public class PostfixCalc {
    Deque<Integer> stack = new ArrayDeque<>();

    public int evaluateExpression(String expression){
        if(expression.isEmpty()){
            expression="0 0 +";
        }
        java.util.StringTokenizer tokens = new java.util.StringTokenizer(expression, " +-/*%", true);

        scanAndProcessTokens(tokens);

        return stack.pop();
    }

    private void scanAndProcessTokens(java.util.StringTokenizer tokens) {
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.length() == 0)
                continue;
            else if (token.matches("[-+*/%]")) {
                performCalculations(token);
            } else {
                stack.push(Integer.parseInt(token));
            }
        }
    }

    private void performCalculations(String token){
        Integer tmp1=0;
        Integer tmp2=0;
        if(!isNull(stack.peek())) {
            tmp1 = stack.pop();
        }
        if(!isNull(stack.peek())) {
            tmp2 = stack.pop();
        }
        switch(token){
            case "+":{
                stack.push(tmp2+tmp1);
                break;
            }
            case "-":{
                stack.push(tmp2-tmp1);
                break;
            }
            case "*":{
                stack.push(tmp2*tmp1);
                break;
            }
            case "/":{
                try{ stack.push(tmp2/tmp1);}
                catch (ArithmeticException ex){
                    System.out.println(ex.getMessage() + ", ignoring...");
                    //didn't do anything with it, so let's put it back, and ignore the zero, just for convenience sake...
                    stack.push(tmp2);
                }
                break;
            }
            case "%":{
                try{stack.push(tmp2%tmp1);}
                catch (ArithmeticException ex){
                    System.out.println(ex.getMessage() + ", ignoring...");
                    //didn't do anything with it, so let's put it back, and ignore the zero, just for convenience sake...
                    stack.push(tmp2);
                }
                break;
            }
            default:
                break;
        }
    }
}
