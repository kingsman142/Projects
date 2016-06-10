import random
from environment import Agent, Environment
from planner import RoutePlanner
from simulator import Simulator

class LearningAgent(Agent):
    """An agent that learns to drive in the smartcab world."""

    def __init__(self, env):
        super(LearningAgent, self).__init__(env)  # sets self.env = env, state = None, next_waypoint = None, and a default color
        self.color = 'red'  # override color
        self.planner = RoutePlanner(self.env, self)  # simple route planner to get next_waypoint
        # TODO: Initialize any additional variables here
        from collections import defaultdict
        self.qTable = defaultdict(int)
        self.wins = 0
        self.gamma = 1.0
        self.total_reward = 0
        
        #Initialize all qTable values to 10.0 so we can implement optimistic initialization instead of random exploration
        for a in [None, 'left', 'forward', 'right']:
            for b in ['red', 'green']:
                for c in [None, 'left', 'forward', 'right']:
                    for d in [None, 'left', 'forward', 'right']:
                        for e in [None, 'left', 'forward', 'right']:
                            self.qTable[((a, b, c, d), e)] = 10.0
                            
        self.errors = 0

    def reset(self, destination=None):
        self.planner.route_to(destination)
        # TODO: Prepare for a new trip; reset any variables here, if required

    def update(self, t):
        # Gather inputs
        self.next_waypoint = self.planner.next_waypoint()  # from route planner, also displayed by simulator
        inputs = self.env.sense(self)
        deadline = self.env.get_deadline(self)

        # TODO: Update state
        #State consists of the agent's next waypoint, the status of the light (red or green),
        #traffic to the left (None, forward, left, right), and oncoming traffic (None, forward, left, right).
        #This leads to 4*2*4*4 = 128 possible states in our game! (this means quick learning because of the curse of dimensionality)
        self.state = (self.next_waypoint, inputs['light'], inputs['oncoming'], inputs['left'])
        
        # TODO: Select action according to your policy
        #list_of_actions stores the Q-values after all possible actions in the current state
        list_of_actions = {None: self.qTable[(self.state, None)], 'forward': self.qTable[(self.state, 'forward')], 'left': self.qTable[(self.state, 'left')], 'right': self.qTable[(self.state, 'right')]}
        #list_of_actions2 sorts list_of_actions and consists of the sorted order of actions with the last element being the "best"        
        list_of_actions2 = sorted(list_of_actions, key=list_of_actions.__getitem__)
        
        #Print output
        print "\n",list_of_actions
        print list_of_actions2
        print list_of_actions2[2],": ",list_of_actions[list_of_actions2[2]]
        print list_of_actions2[3],": ",list_of_actions[list_of_actions2[3]]
        
        #Assign action        
        action = list_of_actions2[3]
    
        # Execute action and get reward
        reward = self.env.act(self, action)
        
        self.total_reward += reward
        if reward >= 10: #This is essentially the winning statement
            self.wins += 1
           
        #Considering the average 96 wins, by the 56th win, there should be 60 trials.
        #So, within these last 40 trials, let's count how many errors there are
        if self.wins >= 56:
            if reward < 0:
                self.errors += 1
        #More ouput
        print "Next waypoint: ", self.next_waypoint
        print "Chosen action: ", action
        print "Reward: ", reward

        # TODO: Learn policy based on state, action, reward
        self.alpha = 1.0 #Learning rate - keep it at 1.0 so we rely more on the most recent information of a state and action pair
        self.gamma -= .005 if self.gamma >= .2 else 0 #Don't let gamma go below .2
        #Next 3 lines store the values for the agent's next state
        self.next_inputs = self.env.sense(self)
        self.next_waypoint = self.planner.next_waypoint()
        self.next_state = (self.next_waypoint, inputs['light'], inputs['right'], inputs['oncoming'])
        #Update the Q-table with the equation: Q(s, a) = r(s, a) + gamma*max(s', a')        
        self.qTable[(self.state, action)] = (1.0 - self.alpha)*self.qTable[(self.state, action)] + self.alpha*(reward + self.gamma*max(self.qTable[(self.next_state, None)], self.qTable[(self.next_state, 'forward')], self.qTable[(self.next_state, 'left')], self.qTable[(self.next_state, 'right')]))     
        
        #More output
        #print "LearningAgent.update(): deadline = {}, inputs = {}, action = {}, reward = {}".format(deadline, inputs, action, reward)  # [debug]
        print "Wins: ", self.wins
        print "Gamma: ", self.gamma
        print "Total reward: ", self.total_reward
        print "Errors: ", self.errors

def run():
    """Run the agent for a finite number of trials."""

    # Set up environment and agent
    e = Environment()  # create environment (also adds some dummy traffic)
    a = e.create_agent(LearningAgent)  # create agent
    e.set_primary_agent(a, enforce_deadline=True)  # set agent to track

    # Now simulate it
    sim = Simulator(e, update_delay=.01)  # reduce update_delay to speed up simulation
    sim.run(n_trials=100)  # press Esc or close pygame window to quit


if __name__ == '__main__':
    run()
