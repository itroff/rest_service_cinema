import java.util.Scanner;

class main {
    enum State {
        WAIT, BUY, FILL1, FILL2, FILL3, FILL4
    }

    int dollars = 550;
    int water = 400;
    int milk = 540;
    int beans = 120;
    int cups = 9;
    State state = State.WAIT;

    public void printState() {
        System.out.println("The coffee machine has:");
        System.out.println(water + " ml of water");
        System.out.println(milk + " ml of milk");
        System.out.println(beans + " g of coffee beans");
        System.out.println(cups + " disposable cups");
        System.out.println("$" + dollars + " of money");
    }

    public boolean check(int waterNeed, int milkNeed, int beansNeed) {
        if (water - waterNeed < 0) {
            System.out.println("Sorry, not enough water!");
            return false;
        }
        if (milk - milkNeed < 0) {
            System.out.println("Sorry, not enough milk!");
            return false;
        }
        if (beans - beansNeed < 0) {
            System.out.println("Sorry, not enough coffee beans!");
            return false;
        }
        if (cups - 1 < 0) {
            System.out.println("Sorry, not enough disposable cups!");
            return false;
        }
        return true;
    }

    public boolean communicate(String cmd) {
        if (cmd.equals("buy") && state == State.WAIT) {
            System.out.println(
                    "What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ");
            state = State.BUY;
        } else if (state == State.BUY) {
            String coffee = cmd;
            boolean make = true;
            switch (coffee) {
                case "1":
                    if (check(250, 0, 16)) {
                        dollars += 4;
                        water -= 250;
                        beans -= 16;
                    } else {
                        make = false;
                    }
                    break;
                case "2":
                    if (check(350, 75, 20)) {
                        dollars += 7;
                        water -= 350;
                        milk -= 75;
                        beans -= 20;
                    } else {
                        make = false;
                    }
                    break;
                case "3":
                    if (check(200, 100, 12)) {
                        dollars += 6;
                        water -= 200;
                        milk -= 100;
                        beans -= 12;
                    } else {
                        make = false;
                    }
                    break;
                case "back":
                    state = State.WAIT;
                    make = false;
                    break;
                default:
                    make = false;
                    break;
            }
            if (make) {
                cups--;
                System.out.println("I have enough resources, making you a coffee!");
            }
            state = State.WAIT;
        } else if (cmd.equals("fill") && state == State.WAIT) {
            System.out.println("Write how many ml of water you want to add:");
            state = State.FILL1;
        } else if (state == State.FILL1) {
            water += Integer.parseInt(cmd);
            System.out.println("Write how many ml of milk you want to add:");
            state = State.FILL2;
        } else if (state == State.FILL2) {
            milk += Integer.parseInt(cmd);
            System.out.println("Write how many grams of coffee beans you want to add:");
            state = State.FILL3;
        } else if (state == State.FILL3) {
            beans += Integer.parseInt(cmd);
            System.out.println("Write how many disposable cups of coffee you want to add:");
            state = State.FILL4;
        } else if (state == State.FILL4) {
            cups += Integer.parseInt(cmd);
            state = State.WAIT;
        } else if (cmd.equals("take") && state == State.WAIT) {
            System.out.println("I gave you $" + dollars);
            dollars = 0;
        } else if (cmd.equals("remaining") && state == State.WAIT) {
            printState();
        } else if (cmd.equals("exit") && state == State.WAIT) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        main mn = new main();
        boolean run = true;
        while (run) {
            if (mn.state == State.WAIT) {
                System.out.println();
                System.out.println("Write action (buy, fill, take, remaining, exit):");
            }

            String cmd = scanner.next();
            run = mn.communicate(cmd);

        }
    }

}