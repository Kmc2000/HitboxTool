package com.kjrm;

import java.util.Scanner;

public class converter {

    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Enter image width (px)...");
        int width = Integer.parseInt(in.nextLine());
        System.out.println("Enter image height (px)...");
        int height = Integer.parseInt(in.nextLine());
        System.out.println("Paste coordinates to convert to BYOND format.");
        String line = "";
        while(in.hasNext()){
            line += in.nextLine();
            line += " ";
        }
        String[] parsed = line.split(" ");
        String out = "collision_positions = list(";
        for(int I=0; I < parsed.length; I++){
            String[] next = parsed[I].split(",");
            int x = Integer.parseInt(next[0]);
            int y = Integer.parseInt(next[1]);
            y = (height-y)-(height/2); //We flip the Y around, because java and byond use a different Y origin
            x = x-(width/2);
            out += "new /datum/vector2d("+x+","+y+")";
            if(I < parsed.length-1){
                out += ", ";
            }
        }
        out += ")";
        System.out.println(out);
    }

}
