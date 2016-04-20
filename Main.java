import java.util.Scanner;
import java.lang.Math;

public class Main {

	public static final int LIGHT = 20;
	public static final int NORMAL = 40;
	public static final int HEAVY = 60;

	public static Scanner s = new Scanner(System.in);
	public static void main(String[] args){
		String action; 
		double playerHP, enemyHP, playerBalance, enemyBalance;
		boolean isDefending = false;
		playerHP = enemyHP = 200;
		playerBalance = enemyBalance = 50;
		System.out.println("Type \"help\" for a list of commands.");
		do{
			System.out.println("----------\nYou are facing off against Slime.\n"
					+ "P.HP      - "+ Math.round(playerHP) +"     "+ Math.round(enemyHP) +" -      E.HP\n"
					+ "P.Balance - "+ Math.round(playerBalance) +"      "+ Math.round(enemyBalance) +" - E.Balance");
			action = getCommand();
			switch(action){
			case "help": System.out.println("Commands:\n"
					+ "lattack: Light attack. Low damage and balance cost.\n"
					+ "nattack: Normal attack. Normal damage and balance cost.\n"
					+ "hattack: Heavy attack. High damage and balance cost.\n"
					+ "defend: Reduce damage taken next turn.\n"
					+ "recover: Recover your balance. Does nothing."); break;
			case "lattack":{
				if (playerBalance < 24){
					System.out.println("You are off-balance and fail your attack."); break;
				}
				if (rollEvade(getEvade(enemyBalance))){ //if the player's attack hits
					double damage = LIGHT * (Math.random() * 0.6 + 0.7);
					double balanceDamage = LIGHT * (Math.random() + 0.7);
					System.out.println("Dealt "+ Math.round(damage) +" damage! Slime lost "+ Math.round(balanceDamage) +" balance.");
					enemyHP -= damage; //deal damage with 30% variance up or down
					playerBalance -= LIGHT / 2.5; //subtract player balance, half of base value
					enemyBalance -= balanceDamage; //subtract enemy balance, random from 0-100% of base value
				} else playerBalance -= LIGHT * (Math.random() * 0.8 + 0.4); //on miss deduct balance with 30% variation
			} break;
			case "nattack":{
				if (playerBalance < 24){
					System.out.println("You are off-balance and fail your attack."); break;
				}
				if (rollEvade(getEvade(enemyBalance))){ //parse for normal and heavy attacks
					double damage = NORMAL * (Math.random() * 0.6 + 0.7);
					double balanceDamage = NORMAL * (Math.random() + 0.7);
					System.out.println("Dealt "+ Math.round(damage) +" damage! Slime lost "+ Math.round(balanceDamage) +" balance.");
					enemyHP -= damage; //deal damage with 30% variance up or down
					playerBalance -= NORMAL / 2.5;
					enemyBalance -= balanceDamage;
				} else playerBalance -= NORMAL * (Math.random() * 0.8 + 0.4);
			} break;
			case "hattack":{
				if (playerBalance < 24){
					System.out.println("You are off-balance and fail your attack."); break;
				}
				if (rollEvade(getEvade(enemyBalance))){
					double damage = NORMAL * (Math.random() * 0.6 + 0.7);
					double balanceDamage = HEAVY * (Math.random() + 0.7);
					System.out.println("Dealt "+ Math.round(damage) +" damage! Slime lost "+ Math.round(balanceDamage) +" balance.");
					enemyHP -= damage; //deal damage with 30% variance up or down
					playerBalance -= HEAVY / 2.5;
					enemyBalance -= balanceDamage;
				} else playerBalance -= HEAVY * (Math.random() * 0.8 + 0.4);
			} break;
			case "defend":{
				isDefending = true;
				System.out.println("You take up a defensive position.");
			} break;
			case "recover":{
				double gain = (100 - playerBalance) * (Math.random() * 0.7 + 0.2);
				playerBalance += gain;
				System.out.println("You stepped back and regained your balance. You gained "+ Math.round(gain) +" balance.");
			} break;
			}
			if (!action.equals("help")){
				if (enemyHP <= 0||playerHP <= 0) break;
				if (enemyBalance < 0) enemyBalance = 0; 
				if (playerBalance < 0) playerBalance = 0;
				if (playerBalance > 100) playerBalance = 100;
				if (Math.random() > 0.3){ //slime uses damage attack with 70% chance
					if (rollEEvade(getEvade(playerBalance))){ //accuracy check
						double damage = 20 * (Math.random() + 0.5) * 1.5;
						double balanceDamage = 30 / 2 * Math.random();
						if (isDefending){
							damage *= 0.2;
							balanceDamage *= 0.2;
							isDefending = false;
							System.out.print("Your damage and balance damage taken are greatly reduced this turn. ");
						}
						playerHP -= damage;
						playerBalance -= balanceDamage;
						System.out.println("Slime slapped you. You received "+ Math.round(damage) +" damage and lost "+ Math.round(balanceDamage) +" balance.");
					} else {
						System.out.println("Slime tried to slap you, but you skillfully dodged the attack.");
					}
				} else { //otherwise he uses balance damaging move
					if (rollEEvade(getEvade(playerBalance))){
						System.out.println("Slime rolled forwards and wrapped itself around your feet, knocking you off your balance. Your balance was halved.");
						playerBalance /= 2;
					} else {
						System.out.println("Slime tried to entangle you, but you skillfully dodged the attack.");
					}
				}
				if (enemyBalance < 20){
					System.out.print("Slime tried to pull itself together. ");
					if (Math.random() > 0.6){
						System.out.println("It succeeded, recovering "+ Math.round((100 - enemyBalance) / 2) +" balance.");
						enemyBalance += (100 - enemyBalance) / 2;
					} else System.out.println("But it failed.");
				}
				if (enemyBalance < 0) enemyBalance = 0; 
				if (playerBalance < 0) playerBalance = 0;
				double balanceGain = (Math.random() * 0.5 + 0.5) * 5; //recover balance for both player and enemy
				playerBalance += balanceGain; 
				System.out.println("You recovered "+ Math.round(balanceGain) +" balance.");
				balanceGain = (Math.random() * 0.5 + 0.5) * 2;
				enemyBalance += balanceGain; 
				System.out.println("Slime recovered "+ Math.round(balanceGain) +" balance.");
				if (enemyBalance > 100) enemyBalance = 100;
				if (playerBalance > 100) playerBalance = 100;
			}
		} while(enemyHP > 0&&playerHP > 0);
		if (playerHP > 0) System.out.println("You won!");
		else System.out.println("You lost.");
	}

	public static boolean rollEvade(double evade){
		if (Math.random() > evade / 100){ //roll and return true if you hit an attack with the given evade value
			System.out.print("Hit! ");
			return true;
		} else {
			System.out.print("Miss! ");
			return false;
		}
	}
	
	public static boolean rollEEvade(double evade){
		if (Math.random() > evade / 100){ //roll and return true if you hit an attack with the given evade value
			System.out.print("You were hit! ");
			return true;
		} else {
			System.out.print("Enemy missed! ");
			return false;
		}
	}

	public static double getEvade(double balance){
		return balance * 0.7 + 5; //up to 70% bonus dodge from balance with 5% base
	}

	public static String getCommand(){
		String command;
		do{
			System.out.println("Enter a command.");
			command = s.nextLine();
			if (isValidCommand(command)) break;
			System.out.print("That's not a valid command. ");
		} while(true);
		return command;
	}

	public static boolean isValidCommand(String s){
		String q = s;
		q.toLowerCase().trim();
		return q.equals("help")||q.equals("lattack")||q.equals("nattack")||q.equals("hattack")||q.equals("recover")||q.equals("defend");
	}
}