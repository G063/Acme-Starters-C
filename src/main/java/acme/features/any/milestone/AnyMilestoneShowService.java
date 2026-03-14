package acme.features.any.milestone;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Milestone;

public class AnyMilestoneShowService extends AbstractService<Any, Milestone>{
	 @Autowired
	    protected AnyMilestoneRepository repository;
	    protected Milestone milestone;

	    @Override
	    public void authorise() {
	        super.getResponse().setAuthorised(true);
	    }

	    @Override
	    public void load() {

	        int id;

	        id = super.getRequest().getData("id", int.class);
	        this.milestone = this.repository.findMilestoneById(id);

	    }
	    
	    @Override
	    public void unbind() {


	        super.unbindObject(milestone,
	        		"title",
	                "achievements",
	                "effort",
	                "kind");	

	    }
}
