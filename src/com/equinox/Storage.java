package com.equinox;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import java.util.Date;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

/**
 * Handles the storing of an instance of Memory into a file in JSON formatting, 
 * as well as the retrieving of an instance of Memory from a file in JSON formatting.
 * 
 * @author Jonathan Lim Siu Chi || ign3sc3nc3
 *
 */

public class Storage {
	private static String DEFAULT_FILE_PATH = "storageFile.json";

	private File storageFile;

	private static PrintWriter writer;
	private static Scanner reader;

	public Storage() {
		this.storageFile = new File(DEFAULT_FILE_PATH);
		createFileIfNonExistent();
	}

	public Storage(String filePath) {
		this.storageFile = new File(filePath);
		createFileIfNonExistent();
	}

	private void initialiseReader() {
		try {
			reader = new Scanner(storageFile);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initialiseWriter() {
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(
					storageFile, false)));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void tearDownReader() {
		reader.close();
	}

	private void createFileIfNonExistent() {
		try {
			if (!storageFile.exists()) {
				storageFile.createNewFile();
				storeMemoryToFile(new Memory());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void tearDownWriter() {
		writer.close();
	}

	/**
	 * Stores the memory object that is passed in into a file in JSON formatting
	 * 
	 * @param memoryToStore
	 */
	public void storeMemoryToFile(Memory memoryToStore) {
		initialiseWriter();
		String jsonString = exportAsJson(memoryToStore);
		writer.println(jsonString);
		tearDownWriter();
	}

	public Memory retrieveMemoryFromFile() {
		initialiseReader();
		StringBuilder builder = new StringBuilder();

		while (reader.hasNextLine()) {
			builder.append(reader.nextLine() + "\n");
		}
		String jsonString = builder.toString();
		tearDownReader();
		return importFromJson(jsonString);
	}

	/**
	 * Method to export the current memory into a JSON String for external
	 * storage in a file
	 * 
	 * @return JSON String
	 */
	public String exportAsJson(Memory mem) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalDate.class,
				new LocalDateTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class,
				new LocalTimeTypeConverter());
		Gson gson = gsonBuilder.setPrettyPrinting().create();
		String jsonString = gson.toJson(mem);
		return jsonString;

	}

	/**
	 * Method to parse a json String representing an instance of memory into an
	 * instance of Memory class
	 * 
	 * @param jsonString
	 *            JSON representation of an instance of memory as String
	 * @return an instance of Memory class
	 */
	public static Memory importFromJson(String jsonString) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(DateTime.class,
				new DateTimeTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalDate.class,
				new LocalDateTypeConverter());
		gsonBuilder.registerTypeAdapter(LocalTime.class,
				new LocalTimeTypeConverter());
		Gson gson = gsonBuilder.create();
		return gson.fromJson(jsonString, Memory.class);
	}

	/**
	 * Converter to serialize and deserialize between org.joda.time.DateTime and
	 * com.google.gson.JsonElement
	 * 
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 * 
	 * @author paradite
	 *
	 */
	private static class DateTimeTypeConverter implements
			JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
		@Override
		public JsonElement serialize(DateTime src, Type srcType,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public DateTime deserialize(JsonElement json, Type type,
				JsonDeserializationContext context) throws JsonParseException {
			try {
				return new DateTime(json.getAsJsonPrimitive().getAsString());
			} catch (IllegalArgumentException e) {
				// Try parsing as java.util.Date instead
				Date date = context.deserialize(json, Date.class);
				return new DateTime(date);
			}
		}

	}

	/**
	 * Converter to serialize and deserialize between org.joda.time.DateTime and
	 * com.google.gson.JsonElement
	 *
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 *
	 */

	private static class LocalDateTypeConverter implements
			JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
		@Override
		public JsonElement serialize(LocalDate src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public LocalDate deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return new LocalDate(json.getAsJsonPrimitive().getAsString());
		}

	}

	/**
	 * Converter class to serialize and deserialize between
	 * org.joda.time.LocalTime and com.google.gson.JsonElement
	 * 
	 * With reference to:
	 * 
	 * https://sites.google.com/site/gson/gson-user-guide
	 * 
	 * @author Jonathan Lim Siu Chi || ign3sc3nc3
	 *
	 */
	private static class LocalTimeTypeConverter implements
			JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
		@Override
		public JsonElement serialize(LocalTime src, Type typeOfSrc,
				JsonSerializationContext context) {
			return new JsonPrimitive(src.toString());
		}

		@Override
		public LocalTime deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return new LocalTime(json.getAsJsonPrimitive().getAsString());
		}

	}

}
