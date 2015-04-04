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
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.*;

public class Zeitgeist {
	private static final String SETTINGS_FILE_NAME = "settings.txt";
	private static final String STORAGE_FILE_NAME = "storageFile.json";

	private static String defaultFileDirectory;
	private static String fileDirectory;
	private static String settingsFilePath;
	private static File settingsFile;
	private static Zeitgeist logic;

	public static Scanner scn = new Scanner(System.in);
	public static StorageHandler storage;
	public Memory memory;

	public Zeitgeist() {
		storage = new StorageHandler.Builder().setDirectoryPath(fileDirectory)
				.setFilePath().build();
		memory = storage.retrieveMemoryFromFile();
		memory.setStorageHandler(storage);
		Parser.initialize();
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
	 * @param args
	 *            contains arguments from the command line at launch. (Not used)
	 */
	public static void main(String[] args) {

		readSettingsFile();
		// Check if a file directory path is passed in through argument
		if (args.length == 1) {
			// Check if file directory path is valid
			String customFileDirPath = args[0];
			processStorageDirectory(customFileDirPath);
		}

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
	 * @param userInput
	 *            input from user, parsed by the Parser.
	 * @return a Signal containing a message to be printed, denoting success or
	 *         failure of the execution.
	 */
	private Signal execute(ParsedInput userInput) {
		Signal processSignal;
		Command c;

		Keywords commandType = userInput.getType();
		if (commandType == null) {
			return new Signal(String.format(
					Signal.GENERIC_INVALID_COMMAND_FORMAT, ""), false);
		} else {

			switch (commandType) {
			case ADD:
				c = new AddCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case DELETE:
				c = new DeleteCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case MARK:
				c = new MarkCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case REDO:
				c = new RedoCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case UNDO:
				c = new UndoCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case EDIT:
				c = new EditCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case DISPLAY:
				c = new DisplayCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case SEARCH:
				c = new SearchCommand(userInput, memory);
				processSignal = c.execute();
				break;

			case EXIT:
				c = new ExitCommand(userInput, memory);
				processSignal = c.execute();
				break;

			default:
				// NOTE: This case should never happen
				processSignal = new Signal(Signal.GENERIC_FATAL_ERROR, false);
				System.exit(-1);
				break;
			}

			return processSignal;
		}

	}

	/**
	 * Processes the custom file directory path specified by user.
	 * 
	 * If the path is valid, and there exists a Storage file (storageFile.json)
	 * at that path, the user will be prompted to overwrite it. 
	 * 
	 * Overwrite: 
	 * If there exists a Storage file at the default path, it will be copied to the
	 * custom path. If there is no Storage file at the default path, the
	 * Storage file at the new path will be appended to. 
	 * 
	 * If user chooses not to overwrite, the default path found in the Settings 
	 * file (settings.txt) will be used.
	 * 
	 * If there is no Storage file at the custom path, and there exists a
	 * Storage file at the default path, the file at the default path will be
	 * copied to the new location.
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 * 
	 * @param customFileDirPath
	 */
	private static void processStorageDirectory(String customFileDirPath) {
		// If valid: copy any existing storageFile from its current location to
		// the
		// new user-specified location. Change the settings file.
		if (isValidDirPath(customFileDirPath)) {
			// Check if there is a file at customFileDirPath and select
			// overwrite option.
			String customFilePath = customFileDirPath + "/" + STORAGE_FILE_NAME;
			File newStorageFile = new File(customFilePath);

			if (newStorageFile.exists()) {
				System.out
						.println("A storage file already exists at user-specified location. "
								+ "Do you wish to overwrite it? Y/N");
				String overwrite = scn.nextLine().toUpperCase();

				// If Y >>
				// If there exists a storageFile.json in the current directory
				// as
				// specified in the settings file, copy it to the custom
				// directory
				if (overwrite.equals("Y")) {
					String storageFilePath = fileDirectory + "/"
							+ STORAGE_FILE_NAME;
					File currentStorageFile = new File(storageFilePath);
					if (currentStorageFile.exists()) {
						copyStorageFile(storageFilePath, customFileDirPath);
						System.out
								.println("Storage file copied to specified location: "
										+ customFileDirPath);
					}
					// Do so regardless of existence of storageFile.json
					modifySettingsFile(customFileDirPath);
					fileDirectory = customFileDirPath;
					System.out
							.println("Directory to save storageFile.json is updated in Settings file to: "
									+ customFileDirPath);
				}
				// If N or any other input >> revert to default path.
				else {
					System.out
							.println("Storage file location is reverted to the default: "
									+ fileDirectory);
				}
			}
			// newStorageFile does not exist.
			else {
				// If there exists a storageFile.json in the current directory
				// as
				// specified in the settings file, copy it to the custom
				// directory
				String storageFilePath = fileDirectory + "/"
						+ STORAGE_FILE_NAME;
				File currentStorageFile = new File(storageFilePath);
				if (currentStorageFile.exists()) {
					copyStorageFile(storageFilePath, customFileDirPath);
					System.out
							.println("Storage file copied to specified location: "
									+ customFileDirPath);
				}
				// So do regardless of existence of storageFile.json
				modifySettingsFile(customFileDirPath);
				fileDirectory = customFileDirPath;
				System.out
						.println("Directory to save storageFile.json is updated in Settings file to: "
								+ customFileDirPath);
			}

		}
		// If customFileDirPath is invalid: revert back to default directory
		else {
			System.out
					.println("User-specified directory for storage is invalid. Storage file location is reverted to the default : "
							+ fileDirectory);
		}
	}
	
	/**
	 * Copies storageFile.json from the storageFilePath to customFileDirPath 
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 * 
	 * @param storageFilePath
	 * @param customFileDirPath
	 */
	private static void copyStorageFile(String storageFilePath,
			String customFileDirPath) {
		String customFilePath = customFileDirPath + "/" + STORAGE_FILE_NAME;
		try {
			Files.copy(Paths.get(storageFilePath), Paths.get(customFilePath),
					REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Modifies the Settings file (settings.txt) by overriding the file with
	 * the path specified by customFileDirPath..
	 * 
	 * @param customFileDirPath
	 */
	private static void modifySettingsFile(String customFileDirPath) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					settingsFile, false));
			writer.write(customFileDirPath);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Attempt to read settings.txt in the default directory.
	 * 
	 * If settings.txt does not exist, it will be created and the default
	 * directory path string will be written into it.
	 * 
	 * If settings.txt exists, the directory for saving storageFile.json will be
	 * read from the file. If the directory read is invalid, the directory of
	 * storageFile.json will be reverted to the default.
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 */
	public static String readSettingsFile() {
		// Set default file directory
		setDefaultFileDirectory();

		// Build settings file path
		settingsFilePath = defaultFileDirectory + "/" + SETTINGS_FILE_NAME;

		// Check if settings file exists
		settingsFile = new File(settingsFilePath);
		BufferedWriter writer;
		try {
			if (!settingsFile.exists()) {
				settingsFile.createNewFile();
				// Write default storage file directory path to settings file
				writer = new BufferedWriter(new FileWriter(settingsFile));
				writer.write(defaultFileDirectory);
				fileDirectory = defaultFileDirectory;
				writer.close();
			}
			// Settings file exists. Read storage file directory path from file.
			else {
				BufferedReader reader = new BufferedReader(new FileReader(
						settingsFile));
				// Read storage directory file path
				String fileDirectoryString = reader.readLine();

				// If storage directory file path is invalid, overwrite settings
				// file
				// with default directory path and set the storage file
				// directory path to default
				if (!isValidDirPath(fileDirectoryString)) {
					writer = new BufferedWriter(new FileWriter(settingsFile,
							false));
					writer.write(defaultFileDirectory);
					writer.close();
					fileDirectory = defaultFileDirectory;
				}
				// If storage file path is valid, set it as file directory
				else {
					fileDirectory = fileDirectoryString;
				}

				reader.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileDirectory;
	}

	/**
	 * Sets the default file directory where storageFile.json is saved as the
	 * same directory the program is being run from.
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 * 
	 */
	public static void setDefaultFileDirectory() {
		defaultFileDirectory = new File("").getAbsolutePath();
	}

	/**
	 * Checks if the string is a valid file directory.
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 * 
	 * @param fileDirectoryString
	 * @return True if string is a valid directory, false otherwise.
	 */
	public static Boolean isValidDirPath(String fileDirectoryString) {
		if (fileDirectoryString == null || fileDirectoryString == "") {
			return false;
		}

		return new File(fileDirectoryString).isDirectory();

	}
}
