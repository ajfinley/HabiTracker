var Alexa = require("alexa-sdk");
var appId = 'amzn1.ask.skill.4bf06049-c118-4aee-944f-026fc7dd1521'; //'amzn1.echo-sdk-ams.app.your-skill-id';
var AWS = require("aws-sdk");
AWS.config.update({endpoint: "https://dynamodb.us-east-1.amazonaws.com"});
var docClient = new AWS.DynamoDB.DocumentClient();
var table = "HabitTracker";

exports.handler = function(event, context, callback) {
    var alexa = Alexa.handler(event, context);
    alexa.appId = appId;
    alexa.dynamoDBTableName = 'RecipeAssistantUsers';
    alexa.registerHandlers(newSessionHandlers, startSelectHandlers, ingredientModeHandlers, directionModeHandlers);
    alexa.execute();
};

var states = {
    SELECTMODE: '_SELECTMODE', // User is selecting a recipe to cook.
    INGREDIENTMODE: '_INGREDIENTMODE',  // Read the ingredients or directions to selected recipe.
    DIRECTIONMODE: '_DIRECTIONMODE'
};

var newSessionHandlers = {
    'NewSession': function() {
        this.handler.state = states.SELECTMODE;
        this.emit(':ask', 'Habit tracker here, what can i do for you?');
        // getTasks = function(session, params, callback) {
        //     docClient.get(params, function(err, data) {
        //         if (err) {
        //             console.error("Unable to read item. Error JSON:" + recipe);
        //             callback(false);
        //         } else {
        //             console.log("GetItem succeeded:" + JSON.stringify(data["Item"], null, 2));
        //             callback(session, data.Item);
        //         }
        //     });
        // }

        // var params = {TableName: table, Key:{"userid":"Test"}};
        // getTasks(this, params, function(session, task) {
        //     if (task) {
        //         session.emit(':ask', task["tasks"][0]["task"]);
        //     } else {
        //         session.emit(':ask', "I'm sorry I couldn't find that recipe, could you try rephrasing or picking something else?");
        //     }
        // });


        // var params = {TableName: table, Key:{"userid":"Test"}, UpdateExpression: "set tasks = :tasks", ExpressionAttributeValues: {":tasks":[{"task":"go to the gym"}]}, ReturnValues:"UPDATED_NEW"};
        // docClient.update(params, function(err, data) {
        //     if (err) {
        //         console.error("Unable to update item. Error JSON:", JSON.stringify(err, null, 2));
        //     } else {
        //         console.log("updated");
        //     }
        // });
    },
    "AMAZON.StopIntent": function() {
      this.emit(':tell', "Goodbye!");  
    },
    "AMAZON.CancelIntent": function() {
      this.emit(':tell', "Goodbye!");  
    },
    'SessionEndedRequest': function () {
        console.log('session ended!');
        this.emit(":tell", "Goodbye!");
    }
};

var startSelectHandlers = Alexa.CreateStateHandler(states.SELECTMODE, {
    'NewSession': function () {
        this.emit('NewSession'); // Uses the handler in newSessionHandlers
    },
    'LaunchRequest': function () {
        this.emit('NewSession'); // Uses the handler in newSessionHandlers
    },
    'AMAZON.HelpIntent': function() {
        var message = 'help message';
        this.emit(':ask', message, message);
    },
    "AMAZON.StopIntent": function() {
        this.emit(":tell", "Goodbye!");   
    },
    'SessionEndedRequest': function () {
        console.log("SESSIONENDEDREQUEST");
        this.emit(':tell', "Goodbye!");
    },
    'ReadAllIntent': function() {
        getTasks = function(session, params, callback) {
            docClient.get(params, function(err, data) {
                if (err) {
                    console.error("Unable to read item. Error JSON:" + recipe);
                    callback(false);
                } else {
                    console.log("GetItem succeeded:" + JSON.stringify(data["Item"], null, 2));
                    callback(session, data.Item);
                }
            });
        }

        var params = {TableName: table, Key:{"userid":"Test"}};
        getTasks(this, params, function(session, task) {
            if (task) {
                message = [];
                for(var i = 0; i < task["tasks"].length; i++) {
                    message.push(task["tasks"][i]["task"]);
                }
                session.emit(':ask', message.join(", "));
            } else {
                session.emit(':ask', "I'm sorry I couldn't find that recipe, could you try rephrasing or picking something else?");
            }
        });
    },
    'Unhandled': function() {
        console.log("UNHANDLED");
        this.emit(':ask', 'unhandled message');
    }
});

var ingredientModeHandlers = Alexa.CreateStateHandler(states.INGREDIENTMODE, {
    'NewSession': function() {
        this.emit(':ask', "Hello you were previously hearing ingredients for " + this.attributes['currentRecipe']['RecipeName'] + ", you can say next ingredient to hear more ingredients, last ingredient to go back, start again to hear all the ingredients again, or main menu if you don\'t want to cook this recipe anymore");
    },
    'LaunchRequest': function() {
        this.emit(':ask', "Hello you were previously hearing ingredients for " + this.attributes['currentRecipe']['RecipeName'] + ", you can say next ingredient to hear more ingredients, last ingredient to go back, start again to hear all the ingredients again, or main menu if you don\'t want to cook this recipe anymore");
    },
    'DirectionsIntent': function() {
        this.handler.state = states.DIRECTIONMODE;
        this.attributes['currentDirection'] = 0;
        this.emit(":ask", "OK i will read the directions, you can say next step to move on, last step to go back, and start again to start over");
    },
    'NextIngredientIntent': function() {
        this.attributes['currentIngredient'] += 1;
        if(this.attributes['currentIngredient'] == this.attributes['ingredients'].length) {
            this.handler.state = states.DIRECTIONMODE;
            this.emit(":ask", this.attributes["ingredients"][this.attributes["currentIngredient"]-1] + "<break time='1s'/>, this was the last ingredient, to begin hearing directions say next step");
        } else {
            this.emit(":ask", this.attributes["ingredients"][this.attributes["currentIngredient"]-1]);
        }
    },
    'BackIngredientIntent': function() {
        this.attributes['currentIngredient'] -= 1;
        if(this.attributes['currentIngredient'] <= 0) {
            this.attributes['currentIngredient'] = 1;
            this.emit(":ask", "you were on the first ingredient, which was " + this.attributes["ingredients"][this.attributes["currentIngredient"]-1] + ", to hear the next ingredient say next ingredient");
        } else {
            this.emit(":ask", this.attributes["ingredients"][this.attributes["currentIngredient"]-1]);
        }
    },
    'StartOverIntent': function() {
        this.attributes['currentIngredient'] = 1;
        this.emit(":ask", this.attributes["ingredients"][this.attributes["currentIngredient"]-1]);
    },
    'MainMenuIntent': function() {
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":ask", "What recipe would you like to make?");
    },
    'EndIntent': function() {
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":ask", "What recipe would you like to make?"); 
    },
    'AMAZON.HelpIntent': function() {
        this.emit(':ask', 'You can say next ingredient to hear the next ingredient, last ingredient to go back one ingredient, start again to start the ingredients from the beginning, and main menu to stop making this recipe.');
    },
    "AMAZON.StopIntent": function() {
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":ask", "What recipe would you like to make?"); 
    },
    "AMAZON.CancelIntent": function() {
        console.log("CANCELINTENT");
    },
    'SessionEndedRequest': function () {
        console.log("SESSIONENDEDREQUEST");
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":tell", "Goodbye");
    },
    'Unhandled': function() {
        console.log("UNHANDLED");
        this.emit(':ask', 'Sorry I didn\'t get that. You can say next ingredient to hear the next ingredient, last ingredient to go back one ingredient, start again to start the ingredients from the beginning, and main menu to stop making this recipe.');
    }
});

var directionModeHandlers = Alexa.CreateStateHandler(states.DIRECTIONMODE, {
    'NewSession': function () {
        this.emit(':ask', "Hello you were previously hearing directions for " + this.attributes['currentRecipe']['RecipeName'] + ", you can say next step to hear more directions, last step to go back, start again to hear all the directions again, or main menu if you don\'t want to cook this recipe anymore");
    },
    'LaunchRequest': function() {
        this.emit(':ask', "Hello you were previously hearing ingredients for " + this.attributes['currentRecipe']['RecipeName'] + ", you can say next ingredient to hear more ingredients, last ingredient to go back, start again to hear all the ingredients again, or main menu if you don\'t want to cook this recipe anymore");
    },
    'IngredientsIntent': function() {
        this.handler.state = states.INGREDIENTMODE;
        this.attributes['currentIngredient'] = 0;
        this.emit(":ask", "OK i will read the ingredients, you can say next ingredient to move on, last ingredient to go back, and start again to start over");
    },
    'NextDirectionIntent': function() {
        this.attributes['currentDirection'] += 1;
        if(this.attributes['currentDirection'] == this.attributes['directions'].length) {
            this.emit(":ask", this.attributes["directions"][this.attributes["currentDirection"]-1] + "<break time='1s'/> this was the last direction, if you want to cook something else say main menu to pick a new recipe.");
        } else {
            this.emit(":ask", this.attributes["directions"][this.attributes["currentDirection"]-1]);
        }
    },
    'BackDirectionIntent': function() {
        this.attributes['currentDirection'] -= 1;
        if(this.attributes['currentDirection'] <= 0) {
            this.attributes['currentDirection'] = 1;
            this.emit(":ask", "you were on the first direction, which was " + this.attributes["directions"][this.attributes["currentDirection"]-1] + ", to hear the next direction say next step");
        } else {
            this.emit(":ask", this.attributes["directions"][this.attributes["currentDirection"]-1]);
        }
    },
    'StartOverIntent': function() {
        this.attributes['currentDirection'] = 1;
        this.emit(":ask", this.attributes["directions"][this.attributes["currentDirection"]-1]);
    },
    'MainMenuIntent': function() {
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":ask", "What recipe would you like to make?");
    },
    'EndIntent': function() {
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":ask", "What recipe would you like to make?"); 
    },
    'AMAZON.HelpIntent': function() {
        this.emit(':ask', 'You can say next step to hear the next direction, last step to go back one direction, start again to start the directions from the beginning, and main menu to stop making this recipe.');
    },
    "AMAZON.StopIntent": function() {
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":ask", "What recipe would you like to make?");
    },
    "AMAZON.CancelIntent": function() {
        console.log("CANCELINTENT");
    },
    'SessionEndedRequest': function () {
        console.log("SESSIONENDEDREQUEST");
        this.attributes['currentRecipe'] = {};
        this.attributes['ingredients'] = [];
        this.attributes['directions'] = [];
        this.attributes['currentDirection'] = 0;
        this.attributes['currentIngredient'] = 0;
        this.handler.state = states.SELECTMODE;
        this.emit(":tell", "Goodbye");
    },
    'Unhandled': function() {
        console.log("UNHANDLED");
        this.emit(':ask', 'Sorry, I didn\'t get that. You can say next step to hear the next direction, last step to go back one direction, start again to start the directions from the beginning, and main menu to stop making this recipe.');
    }
});