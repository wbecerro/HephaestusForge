package wbe.hephaestusForge.config;

import org.bukkit.Material;

import java.util.Random;
import java.util.Set;

public class PiglinTradeTier {

    private String id;

    private int totalWeight;

    private Set<PiglinTrade> rewards;

    private Set<Material> interests;

    public PiglinTradeTier(String id, int totalWeight, Set<PiglinTrade> rewards, Set<Material> interests) {
        this.id = id;
        this.totalWeight = totalWeight;
        this.rewards = rewards;
        this.interests = interests;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(int totalWeight) {
        this.totalWeight = totalWeight;
    }

    public Set<PiglinTrade> getRewards() {
        return rewards;
    }

    public void setRewards(Set<PiglinTrade> rewards) {
        this.rewards = rewards;
    }

    public Set<Material> getInterests() {
        return interests;
    }

    public void setInterests(Set<Material> interests) {
        this.interests = interests;
    }

    public PiglinTrade getRandomReward() {
        Random random = new Random();
        double randomNumber = random.nextDouble(totalWeight);
        double weight = 0;

        for(PiglinTrade trade : rewards) {
            weight += trade.getWeight();
            if(randomNumber < weight) {
                return trade;
            }
        }

        return rewards.stream().toList().getLast();
    }
}
