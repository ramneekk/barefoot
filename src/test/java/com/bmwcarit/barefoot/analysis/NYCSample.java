/*
 * Copyright (C) 2015, BMW Car IT GmbH
 *
 * Author: Sebastian Mattheis <sebastian.mattheis@bmw-carit.de>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */

package com.bmwcarit.barefoot.analysis;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import com.bmwcarit.barefoot.util.Tuple;
import com.esri.core.geometry.Point;

public class NYCSample {
    private static List<Tuple<Point, Long>> sources = null;
    private static List<Tuple<Point, Long>> targets = null;

    public static void load() {
        sources = new LinkedList<Tuple<Point, Long>>();
        targets = new LinkedList<Tuple<Point, Long>>();

        try {
            String file =
                    NYCSample.class.getResource("xba96de419e711691b9445d6a6307c170.csv").getFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            Point point = null;
            Long time = null;
            while ((line = reader.readLine()) != null) {
                String[] str = line.split(",");
                try {
                    point = new Point(Double.parseDouble(str[10]), Double.parseDouble(str[11]));
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str[5]).getTime();
                    sources.add(new Tuple<Point, Long>(point, time / 1000));

                    point = new Point(Double.parseDouble(str[12]), Double.parseDouble(str[13]));
                    time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str[6]).getTime();
                    targets.add(new Tuple<Point, Long>(point, time / 1000));
                } catch (ParseException e) {
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Tuple<Point, Long>> sources() {
        if (sources == null) {
            load();
        }
        return sources;
    }

    public static List<Tuple<Point, Long>> targets() {
        if (targets == null) {
            load();
        }
        return targets;
    }
}
