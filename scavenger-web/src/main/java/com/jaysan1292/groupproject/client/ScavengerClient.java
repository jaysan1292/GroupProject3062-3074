package com.jaysan1292.groupproject.client;

import com.jaysan1292.groupproject.data.*;
import com.jaysan1292.groupproject.exceptions.GeneralServiceException;
import com.jaysan1292.groupproject.service.security.AuthorizationException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

import java.net.URI;
import java.util.List;

import static com.jaysan1292.groupproject.client.accessors.Accessors.*;

/** @author Jason Recillo */
@SuppressWarnings("MethodMayBeStatic")
public class ScavengerClient {
    private final Client client;
    private final WebResource root;

    private boolean credentialsSet;

    public ScavengerClient() throws AuthorizationException {
        this(null);
    }

    public ScavengerClient(URI host) throws AuthorizationException {
        this(host, null, null);
    }

    public ScavengerClient(String username, String password) throws AuthorizationException {
        this(null, username, password);
    }

    public ScavengerClient(URI host, String username, String password) throws AuthorizationException {
        client = Client.create();
        if (host == null) host = getDefaultHost(client);
        if ((username != null) && (password != null)) {
            client.addFilter(new HTTPBasicAuthFilter(username, password));
            credentialsSet = true;
        }
        setHost(client, host);
        root = client.resource(host);

        onCreate();
    }

    private void onCreate() throws AuthorizationException {
        // check authentication;
        // service returns 200 OK if credentials are valid
        // service returns 400 Bad Request if credentials are invalid
        if (credentialsSet) {
            ClientResponse response = root.path("auth").get(ClientResponse.class);
            if (response.getStatus() != 200) {
                throw new AuthorizationException("Invalid credentials!");
            }
        }
    }

    //region /players

    public Player getPlayer(long id) throws GeneralServiceException {
        return getPlayerAccessor().get(id);
    }

    public List<Player> getAllPlayers() throws GeneralServiceException {
        return getPlayerAccessor().getAll();
    }

    public List<Player> getPlayersWithoutTeam() throws GeneralServiceException {
        return getPlayerAccessor().getPlayersWithoutTeam();
    }

    public void updatePlayer(Player player) throws GeneralServiceException {
        getPlayerAccessor().update(player);
    }

    public Player createPlayer(Player player) throws GeneralServiceException {
        return getPlayerAccessor().create(player);
    }

    public void deletePlayer(Player player) throws GeneralServiceException {
        getPlayerAccessor().delete(player);
    }

    //endregion

    //region /scavengerhunts

    public ScavengerHunt getScavengerHunt(long id) throws GeneralServiceException {
        return getScavengerHuntAccessor().get(id);
    }

    public List<ScavengerHunt> getAllScavengerHunts() throws GeneralServiceException {
        return getScavengerHuntAccessor().getAll();
    }

    public void updateScavengerHunt(ScavengerHunt scavengerHunt) throws GeneralServiceException {
        getScavengerHuntAccessor().update(scavengerHunt);
    }

    public ScavengerHunt createScavengerHunt(ScavengerHunt scavengerHunt) throws GeneralServiceException {
        return getScavengerHuntAccessor().create(scavengerHunt);
    }

    public void deleteScavengerHunt(ScavengerHunt scavengerHunt) throws GeneralServiceException {
        getScavengerHuntAccessor().delete(scavengerHunt);
    }

    //endregion

    //region /challenges

    public Challenge getChallenge(long id) throws GeneralServiceException {
        return getChallengeAccessor().get(id);
    }

    public List<Challenge> getAllChallenges() throws GeneralServiceException {
        return getChallengeAccessor().getAll();
    }

    public void updateChallenge(Challenge challenge) throws GeneralServiceException {
        getChallengeAccessor().update(challenge);
    }

    public Challenge createChallenge(Challenge challenge) throws GeneralServiceException {
        return getChallengeAccessor().create(challenge);
    }

    public void deleteChallenge(Challenge challenge) throws GeneralServiceException {
        getChallengeAccessor().delete(challenge);
    }

    //endregions

    //region /checkpoints

    public Checkpoint getCheckpoint(long id) throws GeneralServiceException {
        return getCheckpointAccessor().get(id);
    }

    public List<Checkpoint> getAllCheckpoints() throws GeneralServiceException {
        return getCheckpointAccessor().getAll();
    }

    public void updateCheckpoint(Checkpoint checkpoint) throws GeneralServiceException {
        getCheckpointAccessor().update(checkpoint);
    }

    public Checkpoint createCheckpoint(Checkpoint checkpoint) throws GeneralServiceException {
        return getCheckpointAccessor().create(checkpoint);
    }

    public void deleteCheckpoint(Checkpoint checkpoint) throws GeneralServiceException {
        getCheckpointAccessor().delete(checkpoint);
    }

    //endregion

    //region /paths

    public Path getPath(long id) throws GeneralServiceException {
        return getPathAccessor().get(id);
    }

    public List<Path> getAllPaths() throws GeneralServiceException {
        return getPathAccessor().getAll();
    }

    public void updatePath(Path path) throws GeneralServiceException {
        getPathAccessor().update(path);
    }

    public Path createPath(Path path) throws GeneralServiceException {
        return getPathAccessor().create(path);
    }

    public void deletePath(Path path) throws GeneralServiceException {
        getPathAccessor().delete(path);
    }

    //endregion

    //region /teams

    public Team getTeam(long id) throws GeneralServiceException {
        return getTeamAccessor().get(id);
    }

    public List<Team> getAllTeams() throws GeneralServiceException {
        return getTeamAccessor().getAll();
    }

    public void updateTeam(Team team) throws GeneralServiceException {
        getTeamAccessor().update(team);
    }

    public Team createTeam(Team team) throws GeneralServiceException {
        return getTeamAccessor().create(team);
    }

    public void deleteTeam(Team team) throws GeneralServiceException {
        getTeamAccessor().delete(team);
    }

    //endregion
}
