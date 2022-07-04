import sys
import math
import copy

import matplotlib
import matplotlib.pyplot as plt

import pandas as pd

import datetime

def get_plot(data_2D_prepare, max_step = 0.04): # нужна корректировка
    data_2D_prepare.sort(key=lambda row: (row[0]), reverse=False)
    center = (data_2D_prepare[0][0] +  data_2D_prepare[-1][0])/2

    print(f'center: {center}')
    last_index = 0
    data_2D_plots = [[]]
    last_plot_index = 0
    for i in range(len(data_2D_prepare) - 1):
        delta = math.sqrt((data_2D_prepare[i + 1][1] - data_2D_prepare[i][1]) ** 2 + (data_2D_prepare[i + 1][0] - data_2D_prepare[i][0]) ** 2)
        if (delta < max_step or delta > 0.7):
            if(delta < max_step):
                data_2D_plots[last_plot_index].append(data_2D_prepare[i])
        else:
            last_plot_index += 1
            data_2D_plots.append([data_2D_prepare[i]])

    result = None

    for plot in data_2D_plots:
        for point in plot:
            if (center - 0.15 < point[0] < center - 0.1):
                result = copy.deepcopy(plot)
                break
    if (result != None):
        result.sort(key=lambda row: (row[0]), reverse=True)
    else:
        result = copy.deepcopy(data_2D_plots[0])
    result.pop(len(result) - 1)
    return result, len(data_2D_plots)

"""
def get_plot(data_2D_prepare, max_step = 0.04):
    data_2D_prepare.sort(key=lambda row: (row[0]), reverse=False)
    last_index = 0
    data_2D_plots = [[]]
    last_plot_index = 0
    for i in range(len(data_2D_prepare) - 1):
        delta = math.sqrt((data_2D_prepare[i + 1][1] - data_2D_prepare[i][1]) ** 2 + (data_2D_prepare[i + 1][0] - data_2D_prepare[i][0]) ** 2)
        if (delta < max_step or delta > 0.7):
            if(delta < max_step):
                data_2D_plots[last_plot_index].append(data_2D_prepare[i])
        else:
            last_plot_index += 1
            data_2D_plots.append([data_2D_prepare[i]])

    result = None
    for plot in data_2D_plots:
        for point in plot:
            if -0.05 < point[0] < 0.05:
                result = copy.deepcopy(plot)
                break
    if (result != None):
        result.sort(key=lambda row: (row[0]), reverse=True)
    else:
        result = copy.deepcopy(data_2D_plots[0])
    result.pop(len(result) - 1)
    return result, len(data_2D_plots)
"""



def get_average(data):
    x = 0
    y = 0
    for i in data:
        x+= i[0]
        y+= i[1]
    return [x/len(data), y/len(data)]

def one_way_analize(data, averaging_segment_len = 5, indenting_segment_len = 5, significant_angle_1 = 0.2, significant_angle_2 = 1.8, noise_angle = 0.05, not_meaning = 7):
    total_indent_len = averaging_segment_len + indenting_segment_len
    data.sort(key=lambda row: (row[0]), reverse=False)
    
    plots = []
    fractures = [data[0]]
    
    prev_angle = 0
    prev_angle_delta = 0
    current_plot = []
    current_plot += data[0:total_indent_len - 1]
    
    for analize_index in range(total_indent_len, len(data) - total_indent_len):
        left_averaged = get_average(data[analize_index - total_indent_len : analize_index - indenting_segment_len])
        right_averaged = get_average(data[analize_index + indenting_segment_len : analize_index + total_indent_len])
        
        
        
        a1 = (data[analize_index][1] - left_averaged[1])/(data[analize_index][0] - left_averaged[0] + 0.00000001)
        a2 = (data[analize_index][1] - right_averaged[1])/(data[analize_index][0] - right_averaged[0] + 0.00000001)
        
        angle = abs((a1 - a2)/(1 + (a1*a2)))
        #print(f"angle: {angle}")
        #print(f"Prev delta: {prev_angle_delta}")
        
        
        #bug! 
        
        if(prev_angle_delta > 0 and angle <= prev_angle and (abs(angle - prev_angle) > noise_angle or (angle - prev_angle < 0 and abs(angle - prev_angle) > noise_angle*0.5))):
            if (prev_angle > significant_angle_1):
                if (len(current_plot) > not_meaning):  
                    plots.append(current_plot)
                    if(prev_angle >= significant_angle_2 ):

                        fractures.append(current_plot[-1])
                    current_plot = []   
                    
        current_plot.append(data[analize_index])
        
        
        prev_angle_delta = angle - prev_angle
        #print(f"Delta: {prev_angle_delta}\n")
        prev_angle = angle
        
    current_plot += data[len(data) - total_indent_len:]
    plots.append(current_plot)
    fractures.append(data[-1])
    
    array_c = [[plot_id for point in plots[plot_id]] for plot_id in range(len(plots))]
    
    return plots, array_c, fractures

def calculate_info(data_ready, fractures):
    main_lines = [[(fractures[point_id + 1][1] - fractures[point_id][1])/(fractures[point_id + 1][0] - fractures[point_id][0] + 0.0000000001), fractures[point_id][1] - ((fractures[point_id + 1][1] - fractures[point_id][1])/(fractures[point_id + 1][0] - fractures[point_id][0] + 0.000000001))*fractures[point_id][0]] for point_id in range(len(fractures) - 1)]
    cur_line_index = 0
    point_id = 0
    cur_min = 0
    cur_max = 0
    min_max_array = []
    
    while (point_id < len(data_ready)):
        if(data_ready[point_id] != fractures[cur_line_index + 1]):
            distance = (main_lines[cur_line_index][0] * data_ready[point_id][0] - data_ready[point_id][1] + main_lines[cur_line_index][1])/math.sqrt(1 + main_lines[cur_line_index][0]**2)    
            if(cur_min > distance):
                cur_min = distance
            if(cur_max < distance):
                cur_max = distance
        else:
            min_max_array.append([cur_min, cur_max])
            cur_min = 0
            cur_max = 0
            cur_line_index += 1
        point_id += 1

    distances = [math.sqrt((fractures[index][0] - fractures[index + 1][0])**2 + (fractures[index][1] - fractures[index + 1][1])**2) for index in range(len(fractures) - 1)]
    biggest_plot = -1;
    biggest_min_digression = 10;
    biggest_max_digression = -10;
    fracture_id = 0
    plot_coordinates = []
    for index in range(len(distances)):
        fracture_id+=1;
        if(distances[index] > biggest_plot):
            biggest_plot = distances[index]
            print(plot_coordinates)
            plot_coordinates = [fractures[fracture_id - 1], fractures[fracture_id]]
            if(min_max_array[index][0] < biggest_min_digression):
                biggest_min_digression = min_max_array[index][0]
            if(min_max_array[index][1] > biggest_max_digression):
                biggest_max_digression = min_max_array[index][1]
            
        print(f"Range: {distances[index]}")
        print(f"Min digression: {min_max_array[index][0]}")
        print(f"Max digression: {min_max_array[index][1]}")
        
    return biggest_plot, biggest_min_digression, biggest_max_digression, plot_coordinates

#def display_data(plots_2D_1, arrayC, fractures = None, etalon_marker = True):
#    colors= ["r", "g", "b", "c", "y"]
#    plt.axes().set_aspect(1)
#    for i in range(len(plots_2D_1)):
#        x = [j[0] for j in plots_2D_1[i]]
#        y = [j[1] for j in plots_2D_1[i]]
#        с = arrayC
#        plt.plot(
#            x,
#            y,
#            colors[i%5],
#            markersize=2,
#        )
#
#    psevdo_main_lines = [{"line": [fractures[fracture_index], fractures[fracture_index + 1]]} for fracture_index in range(len(fractures) - 1)]
#
#    if(etalon_marker):
#        colors = ["m", "k"]
#        for i in range(len(psevdo_main_lines)):
#            x = [j[0] for j in psevdo_main_lines[i]["line"]]
#            y = [j[1] for j in psevdo_main_lines[i]["line"]]
#            с = arrayC
#            plt.plot(
#                x,
#                y,
#                colors[i%2],
#                markersize=2,
#            )
#
#    plt.show()

def final_analize():
    begining = datetime.datetime.now() 
    start = datetime.datetime.now() 
    data_prepare_3d = pd.read_csv(r"D:\GitHub\EasyScan\src\main\java\com\i4rt\easyscan\lidarControl\FIRST_REALz_.csv")

    name = "x;y;z;angle;refl"
    data_3d = [[float(data_prepare_3d[name][i].split(";")[0]), float(data_prepare_3d[name][i].split(";")[1]), float(data_prepare_3d[name][i].split(";")[2])] for i in range(len(data_prepare_3d[name])) if 10.0 <= float(data_prepare_3d[name][i].split(";")[3]) <= 170.0]

    #data_3d = [[point[0], point[1], point[2]*0.015] for point in data_3d] #потом убрать
    print(data_3d[0])
    
    end = datetime.datetime.now()
    result_time = end - start
    print(f"\n\Readtime: {result_time}")
    
    
    start = datetime.datetime.now()  
    
    cut_standart_vertical = [[item[0], item[1]] for item in data_3d if 0 < item[2] < 0.003] #поправить промежуток
    print(len(cut_standart_vertical))
    data_standard_z, val = get_plot(cut_standart_vertical, max_step = 0.015)
    print(len(data_standard_z))
    
    data_standard_z.sort(key=lambda row: (row[0]), reverse=False)
    
    min_x = data_standard_z[0][0]
    max_x = data_standard_z[-1][0]
    
    data_standard_z.sort(key=lambda row: (row[1]), reverse=False)
    min_y = data_standard_z[0][1]
    max_y = data_standard_z[-1][1]
    
    data_3d = [point for point in data_3d if ( min_x - 0.1 < point[0] < max_x + 0.1 and min_y - 0.1 < point[1] < max_y + 0.1 )]
    print(len(data_3d))
    
    
    data_standart_z_analized, array_c, fractures_standart_z = one_way_analize(data_standard_z, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    print(fractures_standart_z)
    
    #display_data(data_standart_z_analized, array_c, fractures_standart_z, True)
    
    
    biggest_plot, biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_standard_z, fractures_standart_z)
    
    result = {"h1":0, "h2":0, "h3":0, "l1":0, "l2":0, "l3":0, "max_digression": 0, "min_digression": 0, "time": None}
    
    cut_l1 = [[item[2], item[1]] for item in data_3d if plot_coordinates[0][0] + 0.05 - 0.0016 < item[0] < plot_coordinates[0][0] + 0.05 + 0.0016]
    cut_l2 = [[item[2], item[1]] for item in data_3d if (plot_coordinates[1][0] - plot_coordinates[0][0])/2 - 0.0016 < item[0] < (plot_coordinates[1][0] - plot_coordinates[0][0])/2 + 0.0016]
    cut_l3 = [[item[2], item[1]] for item in data_3d if plot_coordinates[1][0] - 0.05 - 0.0016 < item[0] < plot_coordinates[1][0] - 0.05 + 0.0016]
    print(plot_coordinates)
    cut_standart_horizontal = [[item[2], item[1]] for item in data_3d if (plot_coordinates[1][0] - plot_coordinates[0][0])/2 - 0.0016 < item[0] < (plot_coordinates[1][0] - plot_coordinates[0][0])/2 + 0.0016]
    print(cut_standart_horizontal[0])
    print(cut_standart_vertical[0])
    data_standart_x, val = get_plot(cut_standart_horizontal, max_step = 0.015)
    print(len(data_standart_x)) #в этой ыункции нужно искать центр относительно существующих точек
    
    data_standart_x_analized, array_c, fractures_standart_x = one_way_analize(data_standart_x, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    #display_data(data_standart_x_analized, array_c, fractures_standart_x, False)
    print("\n\n++++++++++++++++++++++++++++++++++++++++++++++++")
    biggest_plot, biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_standart_x, fractures_standart_x)
    print("++++++++++++++++++++++++++++++++++++++++++++++++\n\n")
    print(calculate_info(data_standart_x, fractures_standart_x))
    print("++++++++++++++++++++++++++++++++++++++++++++++++\n\n")
    
    
    cut_h1 = [[item[0], item[1]] for item in data_3d if plot_coordinates[0][0] + 0.05 - 0.0016 < item[2] < plot_coordinates[0][0] + 0.05 + 0.0016]
    cut_h2 = [[item[0], item[1]] for item in data_3d if (plot_coordinates[1][0] - plot_coordinates[0][0])/2 - 0.0016 < item[2] < (plot_coordinates[1][0] - plot_coordinates[0][0])/2 + 0.0016]
    cut_h3 = [[item[0], item[1]] for item in data_3d if plot_coordinates[1][0] - 0.05 - 0.0016 < item[2] < plot_coordinates[1][0] - 0.05 + 0.0016]
    print("LENZ")
    print(len(cut_h1))
    print(len(cut_h2))
    print(len(cut_h3))
    
    data_l1, val = get_plot(cut_l1, max_step = 0.015)
    data_l1_analized, array_c, fractures_data_l1= one_way_analize(data_l1, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    result["l1"], biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_l1, fractures_data_l1)
    if(biggest_max_digression > result["max_digression"]):
        result["max_digression"] = biggest_max_digression;
    if(biggest_min_digression < result["min_digression"]):
        result["min_digression"] = biggest_min_digression;    
        
        
        
        
    data_l2, val = get_plot(cut_l2, max_step = 0.015)
    data_l2_analized, array_c, fractures_data_l2= one_way_analize(data_l2, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    result["l2"], biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_l2, fractures_data_l2)
    if(biggest_max_digression > result["max_digression"]):
        result["max_digression"] = biggest_max_digression;
    if(biggest_min_digression < result["min_digression"]):
        result["min_digression"] = biggest_min_digression;   
        
        
        
    data_l3, val = get_plot(cut_l3, max_step = 0.015)
    data_l3_analized, array_c, fractures_data_l3 = one_way_analize(data_l3, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    result["l3"], biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_l3, fractures_data_l3)
    if(biggest_max_digression > result["max_digression"]):
        result["max_digression"] = biggest_max_digression;
    if(biggest_min_digression < result["min_digression"]):
        result["min_digression"] = biggest_min_digression;   
        
    data_h1, val = get_plot(cut_h1, max_step = 0.015)
    data_h1_analized, array_c, fractures_data_h1= one_way_analize(data_h1, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    result["h1"], biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_h1, fractures_data_h1)
    if(biggest_max_digression > result["max_digression"]):
        result["max_digression"] = biggest_max_digression;
    if(biggest_min_digression < result["min_digression"]):
        result["min_digression"] = biggest_min_digression;    
        
        
        
        
    data_h2, val = get_plot(cut_h2, max_step = 0.015)
    data_h2_analized, array_c, fractures_data_h2= one_way_analize(data_h2, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    result["h2"], biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_h2, fractures_data_h2)
    if(biggest_max_digression > result["max_digression"]):
        result["max_digression"] = biggest_max_digression;
    if(biggest_min_digression < result["min_digression"]):
        result["min_digression"] = biggest_min_digression;   
        
        
        
    data_h3, val = get_plot(cut_h3, max_step = 0.015)
    data_h3_analized, array_c, fractures_data_h3 = one_way_analize(data_h3, 
                                                        averaging_segment_len = 5, 
                                                        indenting_segment_len = 10, 
                                                        significant_angle_1 = 0.7, 
                                                        significant_angle_2 = 0.5, 
                                                        noise_angle = 0.1,
                                                        not_meaning = 7)
    
    result["h3"], biggest_min_digression, biggest_max_digression, plot_coordinates = calculate_info(data_h3, fractures_data_h3)
    if(biggest_max_digression > result["max_digression"]):
        result["max_digression"] = biggest_max_digression;
    if(biggest_min_digression < result["min_digression"]):
        result["min_digression"] = biggest_min_digression;   
    
    result["time"] = datetime.datetime.now()
    
    end = datetime.datetime.now()
    result_time = end - start
    print(f"\n\nRuntime: {result_time}")
    
    foo_end = datetime.datetime.now()
    result_time = foo_end - begining
    print(f"\n\Total time: {result_time}")
    
    return result

print(final_analize())