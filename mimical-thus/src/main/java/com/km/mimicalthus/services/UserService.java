package com.km.mimicalthus.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.km.mimicalthus.entities.Behavior;
import com.km.mimicalthus.entities.Passion;
import com.km.mimicalthus.entities.User;
import com.km.mimicalthus.entities.UserBehavior;
import com.km.mimicalthus.entities.UserPassion;
import com.km.mimicalthus.model.DatapointType;
import com.km.mimicalthus.model.Mapper;
import com.km.mimicalthus.model.UserBehaviorDTO;
import com.km.mimicalthus.model.UserDTO;
import com.km.mimicalthus.model.UserPassionDTO;
import com.km.mimicalthus.model.UserRegistrationForm;
import com.km.mimicalthus.repositories.BehaviorRepository;
import com.km.mimicalthus.repositories.PassionRepository;
import com.km.mimicalthus.repositories.UserBehaviorRepository;
import com.km.mimicalthus.repositories.UserPassionRepository;
import com.km.mimicalthus.repositories.UserRepository;

@Service
public class UserService {

	private static final Double POWER_FACTOR = 1.5;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PassionRepository passionRepository;

	@Autowired
	private BehaviorRepository behaviorRepository;

	@Autowired
	private UserPassionRepository userPassionRepository;

	@Autowired
	private UserBehaviorRepository userBehaviorRepository;

	public User findByEmail(String email) throws NoSuchElementException {
		User user = userRepository.findByEmail(email);
		if (user == null)
			throw new NoSuchElementException();

		return user;
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}

	public List<UserDTO> findSimilarByEmail(String email, DatapointType dType) throws NoSuchElementException {

		// retrieval of the user and their data
		User user = findByEmail(email);
		UserDTO userDto = enrichUsers(new ArrayList<User>(Arrays.asList(user)), dType).get(0);

		// preselection of list of users to process/compare
		List<User> selectedUsers = selectUsersToProcess(userDto, dType);
		selectedUsers.remove(user);

		// retrieval of users' data
		List<UserDTO> enrichedUsers = enrichUsers(selectedUsers, dType);

		// processing similarity
		processUsers(userDto, enrichedUsers);

		return enrichedUsers;
	}

	private List<UserDTO> enrichUsers(List<User> selectedUsers, DatapointType dType) {

		Map<Integer, UserDTO> toEnrich = new HashMap<Integer, UserDTO>();

		for (User user : selectedUsers) {
			toEnrich.put(user.getUserId(), Mapper.entityToDTO(user));
		}

		if (dType == DatapointType.BEHAVIOR) {
			List<UserBehavior> behaviors = userBehaviorRepository.findByUserIn(selectedUsers);
			for (UserBehavior ub : behaviors) {
				toEnrich.get(ub.getUser().getUserId()).getDataPoints().put(ub.getBehavior().getName(), ub.getValue());
			}
		} else if (dType == DatapointType.PASSION) {
			List<UserPassion> passions = userPassionRepository.findByUserIn(selectedUsers);
			for (UserPassion up : passions) {
				toEnrich.get(up.getUser().getUserId()).getDataPoints().put(up.getPassion().getName(), up.getValue());
			}
		}

		return new ArrayList<>(toEnrich.values());
	}

	private void processUsers(UserDTO user, List<UserDTO> enrichedUsers) {
		for (UserDTO utp : enrichedUsers) {
			utp.setSimilarity(computeSimilarity(user, utp));
		}
		Collections.sort(enrichedUsers);
	}

	private Double computeSimilarity(UserDTO user, UserDTO utp) {
		Double similarity = 0.0;

		Map<String, Integer> dataPointsToCompute = utp.getDataPoints();

		for (Map.Entry<String, Integer> entry : user.getDataPoints().entrySet()) {
			Integer otherDP = dataPointsToCompute.get(entry.getKey());
			if(otherDP != null) {
				similarity += Math.max(0,
						computeDataPointSim((double) entry.getValue(), (double) otherDP));
			}
		}

		return similarity;
	}

	private Double computeDataPointSim(Double v1, Double v2) {
		Double result = Math.min(v1, v2) - Math.abs(v1 - v2) / 5;
		if (result > 0)
			result = Math.pow(result, POWER_FACTOR);
		return result;
	}

	private List<User> selectUsersToProcess(UserDTO userDto, DatapointType dType) {
		return findAll();
	}

	@Transactional(rollbackFor = NoSuchElementException.class)
	public User registration(UserRegistrationForm form) throws NoSuchElementException {
		User user = Mapper.formToEntity(form);
		userRepository.save(user);

		if (form.getPassions() != null) {
			for (UserPassionDTO p : form.getPassions()) {
				Passion pEntity = passionRepository.findByName(p.getName());
				if (pEntity == null)
					throw new NoSuchElementException("Passion " + p.getName() + " not found");
				userPassionRepository.save(Mapper.createUserPassion(user, p, pEntity));
			}
		}

		if (form.getBehaviors() != null) {
			for (UserBehaviorDTO b : form.getBehaviors()) {
				Behavior bEntity = behaviorRepository.findByName(b.getName());
				if (bEntity == null)
					throw new NoSuchElementException("Behavior " + b.getName() + " not found");
				userBehaviorRepository.save(Mapper.createUserBehavior(user, b, bEntity));
			}
		}

		return user;
	}

	public UserDTO findProfileByEmail(String email) {

		User user = findByEmail(email);
		UserDTO userDto = enrichUsers(new ArrayList<User>(Arrays.asList(user)), DatapointType.BEHAVIOR).get(0);
		
		List<UserPassion> passions = userPassionRepository.findByUser(user);
		for (UserPassion up : passions) {
			userDto.getDataPoints().put(up.getPassion().getName(), up.getValue());
		}
	
		return userDto;
	}

}
