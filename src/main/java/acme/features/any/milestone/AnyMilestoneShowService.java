package acme.features.any.milestone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.principals.Any;
import acme.client.services.AbstractService;
import acme.entities.campaign.Milestone;

@Service
public class AnyMilestoneShowService extends AbstractService<Any, Milestone>{
	 @Autowired
	    protected AnyMilestoneRepository repository;
	    protected Milestone milestone;


	    @Override
	    public void load() {

	        int id;

	        id = super.getRequest().getData("id", int.class);
	        this.milestone = this.repository.findPublishedMilestoneById(id);

	    }

	    @Override
	    public void authorise() {
	    		super.setAuthorised(this.milestone != null);
	    }
	    
	    @Override
	    public void unbind() {

	    	if (this.milestone != null)
	    		super.unbindObject(this.milestone,
	        		"title",
	                "achievements",
	                "effort",
	                "kind");	

	    }
}
