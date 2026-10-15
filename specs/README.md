# Specification Specification

 Each played game is defined by a JSON file in this directory. Shipped apps
and servers are bundled with every spec, which can be chosen from at runtime.

Each specification must define the following fields:

 - "spec-version": The version number of this revision of this specification.
 - "name": The pretty-print name of the type of competition.
 - "rules": A link to the document describing the rules of the competition.
 - "stages": A list of objects describing the stages in the match.

Each stage object must have the following fields:

 - "name": The non-pretty-print short name for the stage.
 - "length": The length of the stage in seconds.
 - "type": Can be one of the following:
   - "inherited": Inherits actions from previous object in "stages".
   - "standalone": A custom set of actions for this stage.

Each action must have the following fields:

 - "name": The non-pretty-print name of the action. Actions can have duplicate
           names if they are in different stages. When transmitting complete
	   match data, this is achieved by prepending the name of the stage to
	   the name of the action, with a dash in between.
 - "pretty-name": The pretty-print name to be shown in UIs
 - "type": Can be one of the following
   - "time": An integer holding a count for passed seconds. For example, this
             can be the amount of time spent defending.
   - "int": An integer holding a count for number of times a condition has
            occured. For example, this can be the amount of times of a robot
	    has scored.
   - "radio": A radio selection for a specific action. Creates an additional
              "fields" field in the action object that defines a list of
	      strings that are selection for the radio buttons.

When sending information about a match, there must be the following fields:

 - "hash": The BLAKE2 sum of the nonce provided by the server and the client's
           UID.
 - "team": The number of the team that was being scouted
 - "actions": A list of transmitted actions.
 
Each action in the transmitted match data must have the following fields:

 - "name": The name must be the unique derivable name specified by the
           "actions" field in the game specification.
 - One of the following fields, based on the specified type:
   - "time": Encoded as an integer
   - "int": Encoded as an integer
   - "radio": Encoded as a string

A script is included in this directory to test the validity of specifications.
Run it like so:

 $ clojure -M test-spec.clj <spec>