package de.htwsaar.esch.Codeopolis.Utils;

import de.htwsaar.esch.Codeopolis.DomainModel.Silo;

public class DepotVisualizer {
	private int siloCount;
	private LinkedList<String> siloInfos;
	
	public DepotVisualizer() {
		this.siloInfos = new LinkedList<String>();
	}
	
	public void appendSiloInfo(Silo silo) {
		String header = String.format("Silo %d: %s", siloCount, silo.getGrainType() != null ? silo.getGrainType().toString() : "EMPTY");
		String grainAmount = String.format("Amount of Grain %d units", silo.getFillLevel());
		double fillPercentage = (double) silo.getFillLevel() / silo.getCapacity() * 100;
		
		String fillBar = String.format("%s %.2f filled", buildBar(fillPercentage), fillPercentage);
		String capacity = String.format("Capacity %d unit", silo.getCapacity());
		
		String result = String.format("%s \n %s \n %s \n %s \n", header, grainAmount, fillBar, capacity);
		siloInfos.addLast(result);
	}
	
	public String visualize() {
		this.siloInfos.sort((o1, o2) -> o1.compareTo(o2));
		return String.join("\n", this.siloInfos);
	}
	
	
	private String buildBar(double fillPercentage) {
		StringBuilder builder = new StringBuilder("|");
		
		int fillBarLength = 20;
        int filledBars = (int) (fillPercentage / 100 * fillBarLength);
        int emptyBars = fillBarLength - filledBars;

        builder.append("|");
        for (int filledIndex = 0; filledIndex < filledBars; filledIndex++) {
            builder.append("=");
        }
        for (int emptyIndex = 0; emptyIndex < emptyBars; emptyIndex++) {
            builder.append("-");
        }
        builder.append("| ");
        
        return builder.toString();
	}
}