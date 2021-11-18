package br.com.example.microservice.infraestructure.utils;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class Utils {

	private Utils() {
		
	}
	
	public static <T> void merge(T source, T target) 
	{
	    ModelMapper modelMapper = new ModelMapper();
	    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	    modelMapper.map(source, target);
	}
}
