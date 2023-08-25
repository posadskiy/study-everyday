package dev.posadskiy.kata;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BinomialExpansion {

    public static String expand(String expr) {
        Expression expression = parseExpression(expr);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < expression.degree + 1; ++i) {
            long coeff = (long) (calculateCoefficient(expression.degree, i) * Math.pow(expression.a, expression.degree - i) * Math.pow(expression.b, i));
            if (coeff == 0) continue;
            result.append(coeff > 0 ? "+" : "").append(coefficientForPrint(coeff, expression.degree, i));
            if (i < expression.degree) {
                result.append(expression.x);
            }
            if (i < expression.degree - 1) {
                result.append("^").append(expression.degree - i);
            }
        }
        
        if (result.charAt(0) == '+') {
            result.delete(0, 1);
        }

        return result.toString();
    }

    private static Expression parseExpression(String expressionString) {
        final Expression expression = new Expression();
        final String[] split = expressionString.split("\\^");
        expression.degree = Integer.parseInt(split[1]);

        final Matcher matcher = Pattern.compile("[a-z]").matcher(split[0]);
        matcher.find();
        expression.x = matcher.group(0);
        final String[] coefficients = split[0].replace("(", "").replace(")", "").split(expression.x);
        expression.a = parseCoefficient(coefficients[0]);
        expression.b = parseCoefficient(coefficients[1]);

        return expression;
    }

    private static int parseCoefficient(String coefficient) {
        if ("-".equals(coefficient)) {
            return -1;
        }
        if ("".equals(coefficient)) {
            return 1;
        }

        return Integer.parseInt(coefficient);
    }

    private static long calculateCoefficient(int degree, int number) {
        return factorial(degree) / factorial(number) / factorial(degree - number);
    }
    
    private static String coefficientForPrint(long coefficient, int degree, int i) {
        if (coefficient == 1 && i != degree) {
            return "";
        }
        if (coefficient == -1 && i != degree) {
            return "-";
        }
        
        return String.valueOf(coefficient);
    }

    private static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }

        return result;
    }
}

class Expression {

    public int a;
    public int b;
    public int degree;
    public String x;
}
