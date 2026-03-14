
package acme.features.authenticated.inventor.part;

import javax.annotation.PostConstruct;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.invention.Part;
import acme.realms.Inventor;

@Controller
public class InventorPartController extends AbstractController<Inventor, Part> {

	@PostConstruct
	protected void initialise() {
		super.setMediaType(MediaType.TEXT_HTML);

		// Estos servicios gestionarán las piezas
		super.addBasicCommand("create", InventorPartCreateService.class);
		super.addBasicCommand("list", InventorPartListService.class);
		super.addBasicCommand("show", InventorPartShowService.class);
		super.addBasicCommand("update", InventorPartUpdateService.class);
		super.addBasicCommand("delete", InventorPartDeleteService.class);
	}
}
