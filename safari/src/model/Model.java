package model;

import java.time.LocalTime;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import db.SafariDAO;

public class Model {
	
	SafariDAO dao = new SafariDAO();
	private Graph<Character, DefaultWeightedEdge> mappa;
	private List<Avvistamento> avvistamentiUtili;
	private LocalTime orarioArrivoPrevisto;
	private int numS1=0; String s1=""; // specie numero 1/2/3/4
	private int numS2=0; String s2=""; // nella classifica del
	private int numS3=0; String s3=""; // turista e numero di
	private int numS4=0; String s4=""; // avvistamenti della stessa
	private int numMigrazioni=0;
	private List<Character> best;            //percorso migliore
	private double punteggioBest=0;
	private List<Character> bestSicurezza;   //percorso più sicuro
	private double punteggioSicurezzaBest=-1;
	
	public void creaGrafo() {
		mappa = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.mappa, dao.getVertici());
		for(char v1 : mappa.vertexSet())
			for(char v2 : mappa.vertexSet()) {
				int peso=dao.getPesoArco(v1,v2);
				if(!(v1==(v2)) && peso!=0 && !(mappa.containsEdge(v1, v2)))
					Graphs.addEdgeWithVertices(mappa, v1, v2, peso);	
			}
	}
	
	public List<Character> calcolaBest(String s1, String s2, String s3, String s4, String stagione, char partenza, char arrivo,
			LocalTime orarioPartenza,LocalTime orarioArrivoMin, LocalTime orarioArrivoMax, Boolean palude, Boolean baobab){
		
		//Distinguo caso in cui il turista ha stilato la classifica o ha scelto tutti gli animali
		if(s1.equals("tutti"))
			avvistamentiUtili = new ArrayList<Avvistamento>(dao.getAvvistamentiUtili(stagione));
		else
			avvistamentiUtili = new ArrayList<Avvistamento>(dao.getAvvistamentiUtili(s1, s2, s3, s4, stagione));
		
		List<DefaultWeightedEdge> trattiPercorsi = new ArrayList<>();
		List<Character> parziale= new ArrayList<>();
		
		//reset
		best = new ArrayList<Character>(parziale);
		punteggioBest=0;
		orarioArrivoPrevisto= LocalTime.of(23, 0);
		numS1=0; this.s1=s1;
		numS2=0; this.s2=s2;
		numS3=0; this.s3=s3;
		numS4=0; this.s4=s4;
		numMigrazioni=0;
		
		parziale.add(partenza);
		
		ricorsioneBest(parziale, arrivo, orarioPartenza, orarioArrivoMin, orarioArrivoMax, partenza, 0,0,0,0,0,0, trattiPercorsi, palude, baobab);
		
		return best;
	}
	
	public void ricorsioneBest(List<Character> parziale, char arrivo, LocalTime orario, LocalTime orarioArrivoMin, LocalTime orarioArrivoMax, char part,
			double punteggio, int numS1, int numS2, int numS3, int numS4, int numMigrazioni, List<DefaultWeightedEdge> trattiPercorsi, Boolean palude, Boolean baobab) {
		
		//Vertice di arrivo raggiunto
		if(part==arrivo && orario.isAfter(orarioArrivoMin)) {
			boolean flag=false;
			
			//caso: palude+baobab
			if(palude && baobab) {
				if(parziale.contains('R') && parziale.contains('C') && punteggio>=punteggioBest) {
					//In caso di percorsi con punteggi uguali, preferiamo percorsi piu corti in quanto
					//più densi di avvistamenti
					if(punteggio==punteggioBest) {
						if(orario.isBefore(orarioArrivoPrevisto))
							flag=true;
					}
					else
						flag=true;
				}
			}
			
			//caso: palude
			else if(palude) {
				if(parziale.contains('R') && punteggio>=punteggioBest) {
					if(punteggio==punteggioBest) {
						if(orario.isBefore(orarioArrivoPrevisto))
							flag=true;
					}
					else
						flag=true;
				}
			}
			
			//caso: baobab
			else if(baobab) {
				if(parziale.contains('C') && punteggio>=punteggioBest) {
					if(punteggio==punteggioBest) {
						if(orario.isBefore(orarioArrivoPrevisto))
							flag=true;
					}
					else
						flag=true;
				}
			}
			
			//caso rimanente: no vincoli
			else if(punteggio>=punteggioBest) {
				if(punteggio==punteggioBest) {
					if(orario.isBefore(orarioArrivoPrevisto))
						flag=true;
				}
				else
					flag=true;
			}
			
			if(flag==true) {
				best = new ArrayList<Character>(parziale);
				punteggioBest=punteggio;
				orarioArrivoPrevisto=orario;
				this.numS1=numS1;
				this.numS2=numS2;
				this.numS3=numS3;
				this.numS4=numS4;
				this.numMigrazioni=numMigrazioni;
				
			}
			//No return perche e' permesso passare dal punto di arrivo e continuare il percorso
		}

		
		for(char vicino : Graphs.neighborListOf(this.mappa, part)) {
			double deltaPunteggio=0;
			int deltaNumS1=0;
			int deltaNumS2=0;
			int deltaNumS3=0;
			int deltaNumS4=0;
			int deltaMigrazioni=0;
			
			DefaultWeightedEdge tratto = mappa.getEdge(vicino, part);
			int lungh = (int)mappa.getEdgeWeight(tratto);
			int minuti=lungh*3; //(lungh/20km/h)*60 -> tempo speso per percorrere l'arco
			
			//Ignoro se l'arco è già stato percorso o se percorrendolo sforo l'orario limite
			if(!trattiPercorsi.contains(tratto) && orario.plusMinutes(minuti).isBefore(orarioArrivoMax)) {
			
				parziale.add(vicino);
				trattiPercorsi.add(tratto);
				
				orario = orario.plusMinutes(minuti);
				LocalTime orarioMedio=orario.minusMinutes((int)minuti/2); //istante in cui saremo a metà arco, in modo da
				                                                          //valutare al meglio gli avvistamenti da considerare	
				for(Avvistamento avv : avvistamentiUtili)
					if(checkAvvistamento(avv, part, vicino, orarioMedio)) {
						//Tengo il conto di quanti avvistamenti per ogni specie
						if(avv.getSpecie().equals(s1) || s1.equals("tutti"))
							deltaNumS1++;
						if(avv.getSpecie().equals(s2))
							deltaNumS2++;
						if(avv.getSpecie().equals(s3))
							deltaNumS3++;
						if(avv.getSpecie().equals(s4))
							deltaNumS4++;
						if(avv.getBranco().equals("migrazione"))
							deltaMigrazioni++;
						
						deltaPunteggio += avv.getPunteggio(); //avv.punteggio e' stato settato a 0 se la specie non e' in classifica
					}
				
				punteggio += deltaPunteggio;
				numS1 += deltaNumS1;
				numS2 += deltaNumS2;
				numS3 += deltaNumS3;
				numS4 += deltaNumS4;
				numMigrazioni += deltaMigrazioni;
				
				ricorsioneBest(parziale, arrivo, orario,orarioArrivoMin, orarioArrivoMax,vicino,
					punteggio,numS1,numS2,numS3,numS4,numMigrazioni,trattiPercorsi, palude, baobab);
			
				//backtracking
				orario= orario.minusMinutes(minuti);
				orarioMedio=orarioMedio.minusMinutes((int)minuti/2);
				punteggio -= deltaPunteggio;
				numS1 -= deltaNumS1;
				numS2 -= deltaNumS2;
				numS3 -= deltaNumS3;
				numS4 -= deltaNumS4;
				numMigrazioni -= deltaMigrazioni;
				parziale.remove(parziale.size()-1);
				trattiPercorsi.remove(tratto);
			}
		}
	}

	private boolean checkAvvistamento(Avvistamento avv, Character vicino, Character part, LocalTime orarioMedio) {
		int differenza=0;
		if((avv.getTratta().equals(vicino.toString()+part.toString()) || avv.getTratta().equals(part.toString()+vicino.toString()))) {
			differenza = (avv.getOrario().getHour() - orarioMedio.getHour())*60;
			differenza = differenza + (avv.getOrario().getMinute()-orarioMedio.getMinute());
			if(differenza>=-120 && differenza<=120) //Riteniamo validi gli avvistamenti con uno scarto massimo
				return true;                      //di 2 ore rispetto all'orario corrente
		}
		return false;
	}

	public List<Character> calcolaSicuro(String stagione, char partenza, char arrivo, LocalTime orarioPartenza, LocalTime orarioArrivoMin, LocalTime orarioArrivoMax, Boolean palude, Boolean baobab){
		avvistamentiUtili = new ArrayList<Avvistamento>(dao.getAvvistamentiUtili(stagione)); //stessa funzione, 1 parametro solo
		List<Avvistamento> aggressioni = new ArrayList<Avvistamento>();
		
		for(Avvistamento avv : avvistamentiUtili) {
			if(avv.getAggressione()>0)
				aggressioni.add(avv);
		}
		
		//reset
		bestSicurezza = new ArrayList<Character>();
		punteggioSicurezzaBest=-1;
		punteggioBest=0;
		orarioArrivoPrevisto= LocalTime.of(0, 0);
		
		List<Character> parziale= new ArrayList<>();
		List<DefaultWeightedEdge> trattiPercorsi = new ArrayList<>();
		
		parziale.add(partenza);
		
		ricorsioneAggressioni(parziale, arrivo, orarioPartenza, orarioArrivoMin,orarioArrivoMax, partenza, 0,0, trattiPercorsi, palude, baobab);
		
		return bestSicurezza;
	}
	
	public void ricorsioneAggressioni(List<Character> parziale, char arrivo, LocalTime orario, LocalTime orarioArrivoMin, LocalTime orarioArrivoMax, char part,
			double punteggioAggressioni, double punteggio, List<DefaultWeightedEdge> trattiPercorsi, Boolean palude, Boolean baobab) {
		
		if(part==arrivo && orario.isAfter(orarioArrivoMin)) {
			boolean flag=false;
			
			//caso: palude+baobab
			if(palude && baobab) {                                                                                  //primo percorso trovato
				if(parziale.contains('R') && parziale.contains('C') && (punteggioAggressioni<=punteggioSicurezzaBest || punteggioSicurezzaBest==-1)) {
					//In caso di percorsi con uguale rischio aggressione, preferisco il percorso con
					//più avvistamenti totali di animali di tutte le specie
					if(punteggioAggressioni==punteggioSicurezzaBest) {
						if(punteggio>punteggioBest) 
							flag=true;
					}
					else 
						flag=true;
				}
			}
			
			//caso: palude
			else if(palude) {
				if(parziale.contains('R') && (punteggioAggressioni<=punteggioSicurezzaBest || punteggioSicurezzaBest==-1)) {
					if(punteggioAggressioni==punteggioSicurezzaBest) {
						if(punteggio>punteggioBest) 
							flag=true;
					}
					else
						flag=true;
				}
			}
			
			//caso: baobab
			else if(baobab) {
				if(parziale.contains('C') && (punteggioAggressioni<=punteggioSicurezzaBest || punteggioSicurezzaBest==-1)) {
					if(punteggioAggressioni==punteggioSicurezzaBest) {
						if(punteggio>punteggioBest)
							flag=true;
					}
					else
						flag=true;
				}
			}
			
			//caso: nessun vincolo
			else if(punteggioAggressioni<=punteggioSicurezzaBest || punteggioSicurezzaBest==-1) {
				if(punteggioAggressioni==punteggioSicurezzaBest) {
					if(punteggio>punteggioBest) 
						flag=true;
				}
				else 
					flag=true;
			}
			
			if(flag==true) {
				bestSicurezza = new ArrayList<Character>(parziale);
				punteggioSicurezzaBest=punteggioAggressioni;
				orarioArrivoPrevisto=orario;
			}
		}

		
		for(char vicino : Graphs.neighborListOf(this.mappa, part)) {
			DefaultWeightedEdge tratto = mappa.getEdge(vicino, part);
			int lungh = (int)mappa.getEdgeWeight(tratto);
			int minuti=lungh*3;
			
			if(!trattiPercorsi.contains(tratto) && orario.plusMinutes(minuti).isBefore(orarioArrivoMax)) {	
			
				parziale.add(vicino);
				trattiPercorsi.add(tratto);
				
				orario = orario.plusMinutes(minuti);
				LocalTime orarioMedio=orario.minusMinutes((int)minuti/2);
				
				double deltaPunteggioAggressioni=0;
				double deltaPunteggio=0;
				for(Avvistamento avv : avvistamentiUtili)
					if(checkAvvistamento(avv, part, vicino, orarioMedio)) {
						deltaPunteggioAggressioni+=avv.getAggressione();
						deltaPunteggio += avv.getPunteggio();
					}
				punteggioAggressioni+=deltaPunteggioAggressioni;
				punteggio+=deltaPunteggio;
			
				ricorsioneAggressioni(parziale, arrivo, orario, orarioArrivoMin,orarioArrivoMax,
						vicino,punteggioAggressioni,punteggio,trattiPercorsi, palude, baobab);
				
				//backtracking
				parziale.remove(parziale.size()-1);
				trattiPercorsi.remove(tratto);
				orario= orario.minusMinutes(minuti);
				orarioMedio=orarioMedio.minusMinutes((int)minuti/2);
				punteggioAggressioni -= deltaPunteggioAggressioni;
				punteggio-=deltaPunteggio;
			}
		}
	}
	
	public Map<String, Double> getDensita(String stagione, boolean leone, boolean elefante, boolean rinoceronte,
			boolean bufalo, boolean leopardo, boolean ghepardo, boolean zebra, boolean antilope,
			boolean gnu, boolean ippopotamo, boolean coccodrillo, boolean giraffa, boolean iena) {
		
		Map<String, Double> densita = new HashMap<String, Double>();
		if(leone)
			densita.put("leone",0.0);
		if(elefante)
			densita.put("elefante",0.0);
		if(rinoceronte)
			densita.put("rinoceronte",0.0);
		if(bufalo)
			densita.put("bufalo",0.0);
		if(leopardo)
			densita.put("leopardo",0.0);
		if(ghepardo)
			densita.put("ghepardo",0.0);
		if(zebra)
			densita.put("zebra",0.0);
		if(antilope)
			densita.put("antilope",0.0);
		if(gnu)
			densita.put("gnu",0.0);
		if(ippopotamo)
			densita.put("ippopotamo",0.0);
		if(coccodrillo)
			densita.put("coccodrillo",0.0);
		if(giraffa)
			densita.put("giraffa",0.0);
		if(iena)
			densita.put("iena",0.0);
		
		if(!densita.isEmpty())
			for(Avvistamento a : dao.getAvvistamentiUtili(stagione, densita))
				densita.replace(a.getSpecie(), densita.get(a.getSpecie()) + a.getNumerositaBranco());
		
		for(String specie : densita.keySet())
			densita.replace(specie, densita.get(specie)/1510); //area Masai Mara = 1510 km^2
		
		return densita;
	}
	
	public List<String> getSpecie() {
		return dao.getSpecie();
	}
	
	public LocalTime getOrarioArrivo() {
		return this.orarioArrivoPrevisto;
	}
	
	public double getPunteggioBest() {
		return this.punteggioBest;
	}
	public double getPunteggioSicuroBest() {
		return this.punteggioSicurezzaBest;
	}
	
	public int getNumS1() {
		return numS1;
	}
	public int getNumS2() {
		return numS2;
	}
	public int getNumS3() {
		return numS3;
	}
	public int getNumS4() {
		return numS4;
	}
	public int getNumMigrazioni() {
		return numMigrazioni;
	}
	
}


