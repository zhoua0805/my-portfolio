// Copyright 2020 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.data;

import java.util.Arrays;
import java.util.List;
import java.lang.*; 


//Class representing the two truths and one lie game.
public class Game {
    static final String[] TRUTHS = new String[] {"I play the piano.", 
                    "I don\'t like pineapples on pizza.",
                    "I have never been skydiving before.", 
                    "I am hungry.", 
                    "I visited my friend in Manchester last Christmas."};

    static final String[] LIES = new String[] {"My favourite subject in high school was Calculus.",  
                "I have two brothers.",
                "I was born in the year of the tiger."};

    private String[] twoTruthsOneLie = new String[3];
    private String result = " ";

    public String[] getFacts(){
        return twoTruthsOneLie;
    }

    public String[] getLies(){
        return LIES;
    }
    public String getResult(){
        return result;
    }

    //generate 2 truths and 1 lie
    public void generateFacts() {
        int truth_1_index;
        int truth_2_index;
        int lie_index;

        truth_1_index = (int) (Math.random() * TRUTHS.length);
        do {
            truth_2_index = (int)(Math.random() * TRUTHS.length);
        }while (truth_1_index == truth_2_index);
        
        lie_index = (int)(Math.random() * LIES.length);
        twoTruthsOneLie[0] = TRUTHS[truth_1_index];
        twoTruthsOneLie[1] = TRUTHS[truth_2_index];
        twoTruthsOneLie[2] = LIES[lie_index];
        shuffle(twoTruthsOneLie); //randomize the order of the items in the list
    }

    public void gameWin(){
        result = "Yay! You got it.";
    }

    public void gameLose(){
        result = "Try again :(";
    }

    public void gameInvalid(){
        result = "Invalid input!";
    }
    // shuffle an array using the Durstenfeld shuffle algorithm 
    public void shuffle(String[] array) {
        int randomIndex;
    	String swap;
        for (int i = array.length - 1; i > 0; i--) {
            randomIndex = (int)(Math.random() * (i+1));
            swap = array[randomIndex];
            array[randomIndex] = array[i];
            array[i] = swap;
        }
    }

}
