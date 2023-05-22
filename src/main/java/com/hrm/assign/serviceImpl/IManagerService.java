package com.hrm.assign.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrm.assign.dao.ManagerDAO;
import com.hrm.assign.entity.Employee;
import com.hrm.assign.entity.Manager;
import com.hrm.assign.exception.ManagerException;
import com.hrm.assign.service.ManagerService;

@Service
public class IManagerService implements ManagerService{

	@Autowired
	ManagerDAO managerDAO;
	
	Logger log = LoggerFactory.getLogger(IManagerService.class);
	
	/**
	 * @param emailId @type String 
	 * @return Details of the manager by emailId
	 */
	@Override
	public Manager getMyDetails(String emailId) throws ManagerException{
		
		Optional<Manager> managerOpt = managerDAO.findByEmailId(emailId);
		
		if(managerOpt.isEmpty()) {
			throw new ManagerException("Manager not found in the database");
		}
		
		return managerOpt.get();

	}

	/**
	 *  @param manager @type Manager
	 *  @return Register manager details
	 */
	@Override
	public Manager signUp(Manager manager) {
		
		try {
			
			return managerDAO.save(manager);
			
		}
		catch(Exception e){
			
			log.error("Unable to save manager detials");
			log.debug("Manager detials wasnt saved {}", manager);
			
			throw new ManagerException("Unable to save a manager detials");
		}
	}

	/**
	 *  @param emailId @type String
	 *  @return List of employee details that are working under manager
	 */
	@Override
	public List<Employee> listMyEmployees(String emailId) {

		Manager manager = getMyDetails(emailId);
		
		return manager.getEmployeesList();
	}

	/**
	 *  @param Manager @type Manager
	 *  @return Updated manager detials
	 */
	@Override
	public Manager updateDetails(Manager manager) {
	
		Manager prevManager = getMyDetails(manager.getEmailId());
		
		prevManager.setEmployeesList(manager.getEmployeesList());
		prevManager.setFromDate(manager.getFromDate());
		prevManager.setToDate(manager.getToDate());
		
		log.debug("Updated manager detials {}", manager);
		return managerDAO.save(prevManager);
	}


}
