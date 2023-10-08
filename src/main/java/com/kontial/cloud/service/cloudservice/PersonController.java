package com.kontial.cloud.service.cloudservice;

import com.kontial.cloud.service.cloudservice.model.Person;
import com.kontial.cloud.service.cloudservice.persistence.InMemoryDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("api")
public class PersonController {

	private InMemoryDataSource inMemoryDataSource;

	@Autowired
	public PersonController(InMemoryDataSource inMemoryDataSource) {
		this.inMemoryDataSource = inMemoryDataSource;
	}

	@RequestMapping(value = "/persons", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> persons() {
		List<Person> persons = inMemoryDataSource.getAll();

		// Use Java streams to transform persons into a list of maps with id, name, and birth year
		List<Map<String, Object>> result = persons.stream()
				.map(person -> {
					Map<String, Object> personMap = new HashMap<>();
					personMap.put("id", person.id());
					personMap.put("name", person.name());
					personMap.put("year", person.birthday().getYear()); // Extract the year
					return personMap;
				})
				.collect(Collectors.toList());

		return ResponseEntity.ok(result);
	}

	@RequestMapping(value = "/persons/summary", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Integer>> personsSummary() {
		List<Person> persons = inMemoryDataSource.getAll();

		// Use Java streams to count and sort names
		Map<String, Integer> summary = persons.stream()
				.collect(Collectors.groupingBy(Person::name, Collectors.summingInt(e -> 1)));

		// Sort the summary map by name in ascending order
		Map<String, Integer> sortedSummary = summary.entrySet().stream()
				.sorted(Map.Entry.comparingByKey())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
						(oldValue, newValue) -> oldValue, HashMap::new));

		return ResponseEntity.ok(sortedSummary);
	}

	@RequestMapping(value = "/person", method = RequestMethod.POST)
	public ResponseEntity<Object> addPerson(@RequestBody Person person) {
		// Validate the person data
		if (person == null || !inMemoryDataSource.isValidPerson(person)) {
			return ResponseEntity.badRequest().body("Invalid person data");
		}

		// Check if the ID is unique
		if (!inMemoryDataSource.isPersonIdUnique(person.id())) {
			return ResponseEntity.badRequest().body("Person ID must be unique");
		}

		// Add the person to the in-memory data source
		inMemoryDataSource.addPerson(person);

		return ResponseEntity.ok().build();
	}

}