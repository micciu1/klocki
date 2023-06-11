import java.util.*;

public class Bricks {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Map<Integer, List<String>> instructions = new HashMap<>();
        List<String> box = new ArrayList<>();
        int countInstructions = 0;
        int prevInstruction = -1;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.isEmpty()) {
                break;
            }

            String[] parts = line.split(":");
            int instructionNumber = Integer.parseInt(parts[0]);
            String blockCode = parts[1];

            if(instructionNumber < 0){
                System.out.println("klops");
                System.exit(0);
            }
            
            if(instructionNumber != prevInstruction){
                countInstructions++;
            }

            if(countInstructions>1000){
                System.out.println("klops");
                System.exit(0);
            }
            
            if(blockCode.length()!=4 || !isValidBlockCode(blockCode)){
                System.out.println("klops");
                System.exit(0);
            }

            if (instructionNumber == 0) {
                if(countInstructions>0){
                    countInstructions--;
                }
                box.add(blockCode);
            } else {
                List<String> instructionBlocks = instructions.getOrDefault(instructionNumber, new ArrayList<>());
                instructionBlocks.add(blockCode);
                instructions.put(instructionNumber, instructionBlocks);
            }
            prevInstruction = instructionNumber;
        }

        int usedBlocksStage1 = 0;
        int usedBlocksStage2 = 0;
        int missingBlocks = 0;
        int builtStructures = 0;
        int failedStructures = 0;

        Set<String> usedBlocks = new HashSet<>();

        // Etap 1
        Collection<List<String>> stages1 = getStagesDivisibleByThree(instructions);
        for (List<String> stage : stages1) {
            boolean success = true;
            List<String> missingBlocksSet = new ArrayList<>();
            for (String blockCode : stage) {
                if (box.contains(blockCode) && !usedBlocks.contains(blockCode) && !missingBlocksSet.contains(blockCode)) {
                    box.remove(blockCode);
                    usedBlocks.add(blockCode);
                    System.out.println(usedBlocks);
                    usedBlocksStage1++;
                } else {
                    missingBlocksSet.add(blockCode);
                    success = false;
                }
            }
            if (success) {
                builtStructures++;
            } else {
                missingBlocks += missingBlocksSet.size();
                failedStructures++;
            }
        }

        // Etap 2
        Collection<List<String>> stages2 = getStagesNotDivisibleByThree(instructions);
        if (!stages2.isEmpty()) {
            List<String> availableBlocks = new ArrayList<>(box);
            availableBlocks.removeAll(usedBlocks);
            for (List<String> stage : stages2) {
                boolean success = true;
                List<String> missingBlocksSet = new ArrayList<>();
                List<String> usedBlocksInStage = new ArrayList<>();
                for (String blockCode : stage) {
                    if (availableBlocks.contains(blockCode) && !usedBlocks.contains(blockCode) && !missingBlocksSet.contains(blockCode) && !box.contains(blockCode)) {
                        availableBlocks.remove(blockCode);
                        usedBlocks.add(blockCode);
                        usedBlocksInStage.add(blockCode);
                        usedBlocksStage2++;
                        
                    } else {
                        if (!missingBlocksSet.contains(blockCode)) {
                            missingBlocksSet.add(blockCode);
                        }
                        success = false;
                    }
                }
                usedBlocksStage2 = usedBlocksInStage.size();
                if (success) {
                    builtStructures++;
                } else {
                    missingBlocks += missingBlocksSet.size();
                    failedStructures++;
                }
                
            }
        }

        int remainingBlocks = box.size();

        System.out.println(usedBlocksStage1);
        System.out.println(usedBlocksStage2);
        System.out.println(remainingBlocks);
        System.out.println(missingBlocks);
        System.out.println(builtStructures);
        System.out.println(failedStructures);
        System.exit(0);
    }

    private static boolean isValidBlockCode(String blockCode) {
        for (char c : blockCode.toCharArray()) {
            if (c < 'A' || c > 'N') {
                return false;
            }
        }
        return true;
    }

    private static Collection<List<String>> getStagesDivisibleByThree(Map<Integer, List<String>> instructions) {
        List<List<String>> stages = new ArrayList<>();
        for (int instructionNumber : instructions.keySet()) {
            if (instructionNumber % 3 == 0) {
                stages.add(instructions.get(instructionNumber));
            }
        }
        stages.sort((a, b) -> Integer.compare(b.size(), a.size()));
        return stages;
    }

    private static Collection<List<String>> getStagesNotDivisibleByThree(Map<Integer, List<String>> instructions) {
        List<List<String>> stages = new ArrayList<>();
        for (int instructionNumber : instructions.keySet()) {
            if (instructionNumber % 3 != 0) {
                stages.add(instructions.get(instructionNumber));
            }
        }
        stages.sort((a, b) -> Integer.compare(b.size(), a.size()));
        return stages;
    }
    
}