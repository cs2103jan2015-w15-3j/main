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

import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Scanner;

import org.joda.time.DateTime;

public class Storage {
	
	private static File storageFile = new File("storageFile.json");
	private static PrintWriter writer;
	private static Scanner reader;
	
	private void initialiseReader(){
		try{
			createFileIfNonExistent();
			reader = new Scanner(storageFile);
			
		} catch(IOException e){
			e.printStackTrace();	
		}
	}
	
	private void initialiseWriter(){
		try{
			createFileIfNonExistent();
			writer = new PrintWriter(new BufferedWriter(new FileWriter(storageFile, false)));
			
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void tearDownReader(){
		reader.close();
	}
	private void createFileIfNonExistent(){
		try{
			if(!storageFile.exists()){
				storageFile.createNewFile();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void tearDownWriter(){
			writer.close();
	}

	public void storeMemoryToFile(Memory memoryToStore){
		initialiseWriter();
		String jsonString = exportAsJson(memoryToStore);
		System.out.println("json written: " + jsonString);
		writer.println(jsonString);
		tearDownWriter();
	}
	
	public Memory retrieveMemoryFromFile(){
		initialiseReader();
		StringBuilder builder = new StringBuilder();

		while (reader.hasNextLine()) {
			builder.append(reader.nextLine() + "\n");
		}
		String jsonString = builder.toString();
		System.out.println("jsonString read: " + jsonString);
		tearDownReader();
		return importFromJson(jsonString);
	}
	
	/**
     * Method to export the current memory into a json String for external
     * storage
     * 
     * @return json String
     */
    public String exportAsJson(Memory mem) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class,
                new DateTimeTypeConverter());
        Gson gson = gsonBuilder.setPrettyPrinting().create();
        String jsonString = gson.toJson(mem);
        return jsonString;

    }

    /**
     * Method to parse a json String representing an instance of memory into an
     * instance of Memory class
     * 
     * @param jsonString
     *            json representation of an instance of memory as String
     * @return an instance of Memory class
     */
    public static Memory importFromJson(String jsonString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DateTime.class,
                new DateTimeTypeConverter());
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonString, Memory.class);
    }

    /**
     * Converter to serialize and deserialize between org.joda.time.DateTime and
     * com.google.gson.JsonElement
     * 
     * Written with the help of the Gson user guide at
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
}
