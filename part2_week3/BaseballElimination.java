import edu.princeton.cs.algs4.*;

import java.util.List;

public class BaseballElimination {
    private final int numberOfTeams;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] against;
    private final boolean[] eliminated;
    private final int numberOfGames;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        this.numberOfTeams = in.readInt();
        this.teams = new String[numberOfTeams];
        this.wins = new int[numberOfTeams];
        this.losses = new int[numberOfTeams];
        this.remaining = new int[numberOfTeams];
        this.against = new int[numberOfTeams][numberOfTeams];
        this.eliminated = new boolean[numberOfTeams]; // default false
        this.numberOfGames = getNumberOfGames();

        readGameStatistics(in);
        calculateElimination();
    }

    private int getNumberOfGames() {
        return (numberOfTeams * (numberOfTeams - 1)) / 2;
    }

    private void readGameStatistics(In in) {
        for (int team = 0; team < numberOfTeams; team++) {
            teams[team] = in.readString();
            wins[team] = in.readInt();
            losses[team] = in.readInt();
            remaining[team] = in.readInt();
            for (int against = 0; against < numberOfTeams; against++)
                this.against[team][against] = in.readInt();
        }
    }

    private void calculateElimination() {
        // Loop through all teams for trivial elimination, if not eliminated, calculate flow network
        int currentMaxWins = getCurrentMaxWins();

        for (int team = 0; team < this.numberOfTeams; team++) {
            if (isTriviallyEliminated(team, currentMaxWins))
                this.eliminated[team] = true;
            else
                calculateNonTrivialElimination(team);
        }
    }

    private boolean isTriviallyEliminated(int team, int currentMaxWins) {
        int maxPossibleWinsOfTeam = this.wins[team] + this.remaining[team];
        return (maxPossibleWinsOfTeam < currentMaxWins);
    }

    private void calculateNonTrivialElimination(int team) {
        FlowNetwork flowNetwork = createFlowNetwork(team);
//        FordFulkerson fordFulkerson = new FordFulkerson(flowNetwork, 0, flowNetwork.V() - 1);
//        if (fordFulkerson.value() < this.remaining[team])
//            this.eliminated[team] = true;
    }


    private FlowNetwork createFlowNetwork(int team) {
        StdOut.println("Creating flow network for team " + team);
        int numberOfRemainingGames = this.numberOfGames - this.numberOfTeams + 1;
        int nuberOfRemainingTeams = this.numberOfTeams - 1;
        int numberOfVertices = 1 + numberOfRemainingGames + nuberOfRemainingTeams + 1;

        FlowNetwork flowNetwork = new FlowNetwork(numberOfVertices);

        int source = 0;
        int sink = numberOfVertices - 1;

        int gameVertex = 1;

        // Add edges from source to games and games to teams
        for (int player = 0; player < this.numberOfTeams; player++) {
            if (player == team)
                continue;

            for (int against = player + 1; against < this.numberOfTeams; against++) {
                if (against == team)
                    continue;

                int capacity = this.against[player][against];
                flowNetwork.addEdge(new FlowEdge(source, gameVertex, capacity));
                flowNetwork.addEdge(new FlowEdge(gameVertex, getTeamVertex(player, team, numberOfRemainingGames), Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(gameVertex, getTeamVertex(against, team, numberOfRemainingGames), Double.POSITIVE_INFINITY));
                gameVertex++;
            }

            // Add edges from player to sink
            int capacity = this.wins[team] + this.remaining[team] - this.wins[player];
            flowNetwork.addEdge(new FlowEdge(getTeamVertex(player, team, numberOfRemainingGames), sink, capacity));
        }

        StdOut.println(flowNetwork);
        return flowNetwork;
    }

    private int getTeamVertex(int player, int team, int games) {
        if (player < team)
            return 1 + games + player;
        else
            return 1 + games + player - 1;
    }


    private int getCurrentMaxWins() {
        int currentMaxWins = 0;
        for (int team = 0; team < this.numberOfTeams; team++) {
            if (this.wins[team] > currentMaxWins)
                currentMaxWins = this.wins[team];
        }
        return currentMaxWins;
    }


    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return List.of(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[indexOfTeam(team)];
    }

    private int indexOfTeam(String team) {
        for (int i = 0; i < numberOfTeams; i++)
            if (teams[i].equals(team))
                return i;

        throw new IllegalArgumentException("Team " + team + " does not exist");
    }

    // number of losses for given team
    public int losses(String team) {
        return losses[indexOfTeam(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return remaining[indexOfTeam(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return against[indexOfTeam(team1)][indexOfTeam(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        return null;
    }

    public static void main(String[] args) {

        // Unit tests, using teams4.txt and teams5.txt as on course website
        String teams4File = "test-ressources/part2_week3/teams4.txt";
        String teams5File = "test-ressources/part2_week3/teams5.txt";

        StdOut.println("########### Testing teams4.txt ###########");
        BaseballElimination division4 = new BaseballElimination(teams4File);
        assert division4.numberOfTeams() == 4 : "Number of teams should be 4, was " + division4.numberOfTeams();
        assert division4.teams().equals(List.of("Atlanta", "Philadelphia", "New_York", "Montreal")) : "Teams should be Atlanta, Philadelphia, New_York, Montreal; were " + division4.teams().toString();
        assert division4.wins("Atlanta") == 83 : "Atlanta should have 83 wins, had " + division4.wins("Atlanta");
        assert division4.wins("Philadelphia") == 80 : "Philadelphia should have 80 wins, had " + division4.wins("Philadelphia");
        assert division4.losses("Atlanta") == 71 : "Atlanta should have 71 losses, had " + division4.losses("Atlanta");
        assert division4.losses("Philadelphia") == 79 : "Philadelphia should have 79 losses, had " + division4.losses("Philadelphia");
        assert division4.remaining("Atlanta") == 8 : "Atlanta should have 8 remaining games, had " + division4.remaining("Atlanta");
        assert division4.remaining("Philadelphia") == 3 : "Philadelphia should have 3 remaining games, had " + division4.remaining("Philadelphia");
        assert division4.against("Atlanta", "Philadelphia") == 1 : "Atlanta should have played Philadelphia 1 time, played " + division4.against("Atlanta", "Philadelphia") + " times";
        assert division4.against("Philadelphia", "Atlanta") == 1 : "Philadelphia should have played Atlanta 1 time, played " + division4.against("Philadelphia", "Atlanta") + " times";

        StdOut.println("############ Testing teams5.txt ############");
        BaseballElimination division5 = new BaseballElimination(teams5File);
        assert division5.numberOfTeams() == 5 : "Number of teams should be 5, was " + division5.numberOfTeams();
        assert division5.teams().equals(List.of("New_York", "Baltimore", "Boston", "Toronto", "Detroit")) : "Teams should be New_York, Baltimore, Boston, Toronto, Detroit; were " + division5.teams().toString();
        assert division5.wins("New_York") == 75 : "New_York should have 75 wins, had " + division5.wins("New_York");
        assert division5.wins("Baltimore") == 71 : "Baltimore should have 71 wins, had " + division5.wins("Baltimore");
        assert division5.losses("New_York") == 59 : "New_York should have 59 losses, had " + division5.losses("New_York");
        assert division5.losses("Baltimore") == 63 : "Baltimore should have 63 losses, had " + division5.losses("Baltimore");
        assert division5.remaining("New_York") == 28 : "New_York should have 28 remaining games, had " + division5.remaining("New_York");
        assert division5.remaining("Baltimore") == 28 : "Baltimore should have 28 remaining games, had " + division5.remaining("Baltimore");
        assert division5.against("New_York", "Baltimore") == 3 : "New_York should have played Baltimore 3 time, played " + division5.against("New_York", "Baltimore") + " times";
        assert division5.against("Baltimore", "New_York") == 3 : "Baltimore should have played New_York 3 time, played " + division5.against("Baltimore", "New_York") + " times";

        StdOut.println("All tests passed.");



//        BaseballElimination division = new BaseballElimination(args[0]);
//        for (String team : division.teams()) {
//            if (division.isEliminated(team)) {
//                StdOut.print(team + " is eliminated by the subset R = { ");
//                for (String t : division.certificateOfElimination(team)) {
//                    StdOut.print(t + " ");
//                }
//                StdOut.println("}");
//            }
//            else {
//                StdOut.println(team + " is not eliminated");
//            }
//        }
    }

}
