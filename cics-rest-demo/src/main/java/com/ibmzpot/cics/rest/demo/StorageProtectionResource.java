package com.ibmzpot.cics.rest.demo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import jakarta.json.Json;
import jakarta.json.JsonObject;

import java.io.UnsupportedEncodingException;

import com.ibm.cics.server.Task;
import com.ibm.cics.server.Program;
import com.ibm.cics.server.Channel;
import com.ibm.cics.server.Container;
import com.ibm.cics.server.CicsConditionException;
import com.ibm.cics.server.InvalidRequestException;
import com.ibm.cics.server.LengthErrorException;

@Path("stgprot")
public class StorageProtectionResource {

    private static final String CCSID = System.getProperty("com.ibm.cics.jvmserver.local.ccsid");

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject getStorageProtectionConfig() {
        
        Channel svtt_channel;
        Container svtt_input; 
        Container svtt_cicscfg;    
        String cicscfgString;            

        Program svtt00 = new Program();
        svtt00.setName("SVTT00");
        Task task = Task.getTask();

        try {
            svtt_channel = task.createChannel("SVTT_CHANNEL");
            svtt_input = svtt_channel.createContainer("SVTT_INPUT");
            svtt_input.putString("SVTT"); 
            svtt00.link(svtt_channel);
            svtt_cicscfg = svtt_channel.getContainer("SVTT_CICSCFG");   
    
            if (svtt_cicscfg != null) {                                          // Container found?
                cicscfgString = svtt_cicscfg.getString();
            }
            else {
                cicscfgString = "Unknown";
            }
        }
        catch (CicsConditionException cce) {       
            throw new RuntimeException(cce);
        }
        
        //System.out.println("CICSCFG: " + cicscfgString);

        JsonObject cicscfg = Json.createObjectBuilder()
           .add("cicscfg", cicscfgString)
           .add("rentpgm", "EXAMPLE").build();

        return cicscfg;
    }

}
