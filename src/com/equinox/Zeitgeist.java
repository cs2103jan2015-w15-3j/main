package com.equinox;

/**
 * This is where it all begins.
 * 
 * @author paradite
 *
 */
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;


public class Zeitgeist {
	private static final String SETTINGS_FILE_NAME = "settings.txt";
	
	private static String defaultFileDirectory;
	private static String fileDirectory;
	private static String settingsFilePath;
    private static Zeitgeist logic;

    public static Scanner scn = new Scanner(System.in);
    public static StorageHandler storage;
    public Memory memory;

    public Zeitgeist() {
    	//Builder builds StorageHandler with user-specified file path
    	storage = new StorageHandler.Builder()
    		.setDirectoryPath(defaultFileDirectory)
    		.setFilePath()
    		.build();
        memory = storage.retrieveMemoryFromFile();
    }
    
    public static void setDefaultFileDirectory(){
    	defaultFileDirectory = new File("").getAbsolutePath();
    }
    
    
    public static void readSettingsFile(){
    	//set default file directory
    	setDefaultFileDirectory();
    	
    	//build settings file path
    	settingsFilePath = defaultFileDirectory + "/" + SETTINGS_FILE_NAME;
    	System.out.println(settingsFilePath);
    	//check if settings file exists
    	File settingsFile = new File(settingsFilePath);
    	BufferedWriter writer;
    	try{
	    	if(!settingsFile.exists()){
	    		settingsFile.createNewFile();
	    		//write default file directory to settings file
	    		writer = new BufferedWriter(new FileWriter(settingsFile));
	    		writer.write(defaultFileDirectory);
	    		fileDirectory = defaultFileDirectory;
	    		writer.close();
	    	}
	    	//Settings file exists. Read fileDirectory from file.
	    	else {
	    		BufferedReader reader = new BufferedReader(new FileReader(settingsFile));
		    	//read storage directory file path
		    	String fileDirectoryString = reader.readLine();
		    	
		    	//if storage directory file path is invalid, overwrite settings file
		    	//with default directory path and set the storage file directory to default
		    	if(!isValidFilePath(fileDirectoryString)){
		    		writer = new BufferedWriter(new FileWriter(settingsFile, false));
		    		writer.write(defaultFileDirectory);
		    		writer.close();
		    		fileDirectory = defaultFileDirectory;
		    	}
		    	//if storage file path is valid, set it as file directory
		    	else{
		    		fileDirectory = fileDirectoryString;
		    	}
		    	
		    	reader.close();
	    	}
	    	
    	} catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    public static Boolean isValidFilePath(String fileDirectoryString){
    	if(fileDirectoryString.length()==0){
    		return false;
    	}
    	
    	return new File(fileDirectoryString).isDirectory();
 
    }
    public static Zeitgeist getInstance() {
        if (logic == null) {
            logic = new Zeitgeist();
        }
        return logic;
    }

    public final static void clearConsole() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                for (int i = 0; i < 105; i++) {
                    System.out.println();
                }
            } else {
                final String ANSI_CLS = "\u001b[2J";
                final String ANSI_HOME = "\u001b[H";
                System.out.print(ANSI_CLS + ANSI_HOME);
                System.out.flush();
            }
        } catch (final Exception e) {
            System.out.println("error in clearing");
        }
    }

	/**
	 * The main logic unit of Zeitgeist. Reads the input from Zeitgeist and
	 * passes it to the Parser, the first element in the flow of component calls
	 * present in all operations.
	 * 
	 * @param args contains arguments from the command line at launch. (Not used)
	 */
	public static void main(String[] args) {
		/*
		//Check for filePath argument
		//If args.length is 1, take in the user-specified file path string
		if(args.length == 1){
			filePath = args[0];
			
		}
		*/
    	readSettingsFile();
        SignalHandler.printSignal(new Signal(Signal.WELCOME_SIGNAL, true));
        String input;
        Zeitgeist logic = getInstance();
		while (true) {
            SignalHandler.printCommandPrefix();
            input = scn.nextLine();
            clearConsole();
            Signal signal = logic.handleInput(input);
            SignalHandler.printSignal(signal);
		}
	}

    public Signal handleInput(String input) {
        ParsedInput c = Parser.parseInput(input);
        return execute(c);
    }

	/**
	 * Creates a Command object with the given ParsedInput and executes it.
	 * 
	 * @param userInput input from user, parsed by the Parser.
	 * @return a Signal containing a message to be printed, denoting success or
	 *         failure of the execution.
	 */
    private Signal execute(ParsedInput userInput) {
		Signal processSignal;
		Command c;

		Keywords commandType = userInput.getType();
		if (commandType == null) {
            return new Signal(String.format(Signal.GENERIC_INVALID_COMMAND_FORMAT, ""), false);
		} else {

            switch (commandType) {
                case ADD :
                	c = new AddCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case DELETE :
                	c = new DeleteCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case MARK :
                	c = new MarkCommand(userInput, memory);
                	processSignal = c.execute();
                    break;
				
                case REDO :
                	c = new RedoCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case UNDO :
                	c = new UndoCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case EDIT :
                	c = new EditCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case DISPLAY :
                	c = new DisplayCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case SEARCH :
                	c = new SearchCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                case EXIT :
                	c = new ExitCommand(userInput, memory);
                	processSignal = c.execute();
                    break;

                default :
                	// NOTE: This case should never happen
                    processSignal = new Signal(Signal.GENERIC_FATAL_ERROR,
                            false);
                    System.exit(-1);
                    break;
			}
            
            return processSignal;

		}
		
	}
}
