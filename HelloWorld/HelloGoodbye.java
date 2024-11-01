/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class HelloGoodbye {
    public static void main(String[] args) {
        String hello = "Hello";
        String goodbye = "Goodbye";
        for (int i = 0; i < args.length; i++) {
            if (i == 0) {
                hello += " " + args[i];
                goodbye += " " + args[i];
            }
            else if (i == args.length - 1) {
                hello += " and " + args[i] + ".";
                goodbye += " and " + args[i] + ".";
            }
            else {
                hello += ", " + args[i];
                goodbye += ", " + args[i];
            }
        }
        System.out.println(hello);
        System.out.println(goodbye);
    }
}
