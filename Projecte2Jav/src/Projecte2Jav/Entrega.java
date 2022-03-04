package Projecte2Jav;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="entrega")
public class Entrega {
	private String nomTreballador;
	private ArrayList<Treballador> treballadors = new ArrayList<Treballador>();
	public Entrega() {
	}

	@XmlElementWrapper(name="treballadors")
	@XmlElement(name="treballador")
	public ArrayList<Treballador> getTreballadors(){
		return treballadors;
	}
	
	@XmlElement(name="nomTreballador")
	public String getNomTreballador() {
		return nomTreballador;
	}

	public void setNomTreballador(String nomTreballador) {
		this.nomTreballador = nomTreballador;
	}

	

	public void setTreballadors(ArrayList<Treballador> treballadors) {
		this.treballadors = treballadors;
		
	}
}
