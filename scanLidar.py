import sys
import math
import copy
import datetime
import socket
import time
import csv

import matplotlib
import matplotlib.pyplot as plt

import numpy
import pandas as pd
from sklearn.cluster import DBSCAN


def counting_arrays(string, choice):
    angle_count = 0
    while len(string) > 1:
        dec = int(string[0:4], 16)
        string = string[4:len(string)]
        if choice == 1:
            c_arrays_len.append(float(dec) / 10000)
        elif choice == 2:
            c_arrays_refl.append(dec)
        elif choice == 3:
            c_arrays_angle.append(55 + (angle_count * 0.0833))
            angle_count = angle_count + 1


def counting():
    for j in range(len(c_arrays_len)):
        coord_array = []
        length = c_arrays_len[j]
        angle_rad = c_arrays_angle[j] * math.pi / 180
        x = math.cos(angle_rad) * length
        y = math.sin(angle_rad) * length
        if length != 0:
            coord_array.append(x)
            coord_array.append(y)
            coord_array.append(z)
            arrays_csv.append(x)
            arrays_csv.append(y)
            arrays_csv.append(z)
            arrays.append(coord_array)
    c_arrays_angle.clear()
    c_arrays_len.clear()


def func_chunk(lst):
    for x in range(0, len(lst), 3):
        e_c = lst[x: 3 + x]
        yield e_c


def get_plot_DBSCAN(data_2D_prepare, max_step=0.02, min_samples_got=7):
    db = DBSCAN(eps=max_step, min_samples=min_samples_got).fit(data_2D_prepare)
    center_x = 0
    for point in data_2D_prepare:
        center_x += point[0]
    center_x = center_x / len(data_2D_prepare)

    plots_unique_val, counts = numpy.unique(db.labels_, return_counts=True)
    plots = []
    plot_color_array = []
    for plot_color in plots_unique_val:
        plot = []
        if (plot_color != 1):
            for point_id in range(len(db.labels_)):
                if (db.labels_[point_id] == plot_color):
                    plot.append(data_2D_prepare[point_id])
            plots.append(plot)
            plot_color_array.append(plot_color)

    min_distance = 0.2
    result_plot_id = 0

    for plot_id in range(len(plots)):
        # display_data([plots[plot_id]], [7], etalon_marker = False)
        for point in plots[plot_id]:
            if (abs(point[0] - center_x) < min_distance):
                min_distance = abs(point[0] - center_x)
                result_plot_id = plot_id
    return plots[result_plot_id], len(plots_unique_val)


def get_artificial_data_cut(center, data_3d):
    real_artificial_cut = [[item[1], item[2], item[0]] for item in data_3d if
                           center - 0.001 < item[0] < center + 0.001]
    real_artificial_cut_2D = [[item[1], item[0]] for item in real_artificial_cut]

    real_artificial_cut_2D_grabbed = []
    existing_z = []

    for i in real_artificial_cut_2D:
        if (i[0] in existing_z):
            continue
        else:
            real_artificial_cut_2D_grabbed.append(i)
            existing_z.append(i[0])
    print(len(real_artificial_cut_2D_grabbed))
    # display_data([real_artificial_cut_2D_grabbed], [1 for i in range(len(real_artificial_cut_2D_grabbed))], etalon_marker = False)

    prepared_data, val = get_plot_DBSCAN(real_artificial_cut_2D_grabbed, 0.03, 2)
    prepared_data.sort(key=lambda row: (row[0]), reverse=False)
    # center = (prepared_data[0][0] + prepared_data[-1][0]) / 2 #рассчет середины получившегося среза

    return prepared_data


def get_data_cut(center, data_3d_inside):
    real_center = (center // 0.0015) * 0.0015
    print(f'real center: {real_center}')
    # print(data_3d_inside)

    real_cut = [[item[0], item[1], item[2]] for item in data_3d_inside if
                real_center - 0.0008 < item[2] < real_center + 0.0008]  # changed
    real_cut_2D = [[item[0], item[1]] for item in real_cut]
    print(real_cut[0])

    real_cut_2D_grabbed = []
    existing_z = []

    for i in real_cut_2D:
        if (i[0] in existing_z):
            continue
        else:
            real_cut_2D_grabbed.append(i)
            existing_z.append(i[0])
    print(len(real_cut_2D_grabbed))
    # display_data([real_cut_2D_grabbed], [1 for i in range(len(real_cut_2D_grabbed))], etalon_marker = False)

    prepared_data, val = get_plot_DBSCAN(real_cut_2D_grabbed, 0.015, 5)

    return prepared_data


def display_data(plots_2D_1, arrayC, fractures=None, etalon_marker=True):
    colors = ["r.", "g.", "b.", "c.", "y."]
    plt.axes().set_aspect(1)
    cur_pos_start = 0
    for i in range(len(plots_2D_1)):
        cur_pos_end = cur_pos_start + len(plots_2D_1[i])
        x = [j[0] for j in plots_2D_1[i]]
        y = [j[1] for j in plots_2D_1[i]]
        # с = arrayC[cur_pos_start:cur_pos_end]
        cur_pos_start = cur_pos_end
        if (arrayC[i]) == -1:
            color = "k."
        else:
            color = colors[i % 5]
        # color = colors[i%5]
        plt.plot(
            x,
            y,
            color,
            markersize=2,
        )

    if (etalon_marker):
        psevdo_main_lines = [{"line": [fractures[fracture_index], fractures[fracture_index + 1]]} for fracture_index in
                             range(len(fractures) - 1)]
        colors = ["m", "k"]
        for i in range(len(psevdo_main_lines)):
            x = [j[0] for j in psevdo_main_lines[i]["line"]]
            y = [j[1] for j in psevdo_main_lines[i]["line"]]
            с = arrayC
            plt.plot(
                x,
                y,
                colors[i % 2],
                markersize=2,
            )

    plt.show()


def calculate_info(data_ready, fractures):
    main_lines = [[(fractures[point_id + 1][1] - fractures[point_id][1]) / (
            fractures[point_id + 1][0] - fractures[point_id][0] + 0.0000000001), fractures[point_id][1] - (
                           (fractures[point_id + 1][1] - fractures[point_id][1]) / (
                           fractures[point_id + 1][0] - fractures[point_id][0] + 0.000000001)) *
                   fractures[point_id][0]] for point_id in range(len(fractures) - 1)]
    cur_line_index = 0
    point_id = 0
    cur_min = 0
    cur_max = 0
    min_max_array = []

    while (point_id < len(data_ready)):
        if (data_ready[point_id] != fractures[cur_line_index + 1]):
            distance = (main_lines[cur_line_index][0] * data_ready[point_id][0] - data_ready[point_id][1] +
                        main_lines[cur_line_index][1]) / math.sqrt(1 + main_lines[cur_line_index][0] ** 2)
            if (cur_min > distance):
                cur_min = distance
            if (cur_max < distance):
                cur_max = distance
        else:
            min_max_array.append([cur_min, cur_max])
            cur_min = 0
            cur_max = 0
            cur_line_index += 1
        point_id += 1

    distances = [math.sqrt(
        (fractures[index][0] - fractures[index + 1][0]) ** 2 + (fractures[index][1] - fractures[index + 1][1]) ** 2) for
        index in range(len(fractures) - 1)]
    biggest_plot = -1;
    biggest_min_digression = 10;
    biggest_max_digression = -10;
    fracture_id = 0
    plot_coordinates = []
    for index in range(len(distances)):
        fracture_id += 1;
        if (distances[index] > biggest_plot):
            biggest_plot = distances[index]
            print(plot_coordinates)
            plot_coordinates = [fractures[fracture_id - 1], fractures[fracture_id]]
            if (min_max_array[index][0] < biggest_min_digression):
                biggest_min_digression = min_max_array[index][0]
            if (min_max_array[index][1] > biggest_max_digression):
                biggest_max_digression = min_max_array[index][1]

        print(f"Range: {distances[index]}")
        print(f"Min digression: {min_max_array[index][0]}")
        print(f"Max digression: {min_max_array[index][1]}")

    return biggest_plot, biggest_min_digression, biggest_max_digression, plot_coordinates


def get_average(data):
    x = 0
    y = 0
    for i in data:
        x += i[0]
        y += i[1]
    return [x / len(data), y / len(data)]


def one_way_analize_advanced(data, averaging_segment_len=5, indenting_segment_len=5, significant_angle_1=0.2,
                             significant_angle_2=1.8, noise_angle=0.05, not_meaning=7):
    total_indent_len = averaging_segment_len + indenting_segment_len
    data.sort(key=lambda row: (row[0]), reverse=False)
    left_adder_segment = [data[0] for _ in range(averaging_segment_len + indenting_segment_len)]
    right_adder_segment = [data[-1] for _ in range(averaging_segment_len + indenting_segment_len)]

    data = left_adder_segment + data
    data = data + right_adder_segment

    plots = []
    fractures = [data[0]]

    prev_angle = 0
    prev_angle_delta = 0
    current_plot = []
    current_plot += data[0:total_indent_len - 1]

    for analize_index in range(total_indent_len, len(data) - total_indent_len):
        left_averaged = get_average(data[analize_index - total_indent_len: analize_index - indenting_segment_len])
        right_averaged = get_average(data[analize_index + indenting_segment_len: analize_index + total_indent_len])

        a1 = (data[analize_index][1] - left_averaged[1]) / (data[analize_index][0] - left_averaged[0] + 0.00000001)
        a2 = (data[analize_index][1] - right_averaged[1]) / (data[analize_index][0] - right_averaged[0] + 0.00000001)

        angle = abs((a1 - a2) / (1 + (a1 * a2)))
        # print(f"angle: {angle}")
        # print(f"Prev delta: {prev_angle_delta}")

        # bug!

        if (prev_angle_delta > 0 and angle <= prev_angle and (abs(angle - prev_angle) > noise_angle or (
                angle - prev_angle < 0 and abs(angle - prev_angle) > noise_angle * 0.5))):
            if (prev_angle > significant_angle_1):
                if (len(current_plot) > not_meaning):
                    plots.append(current_plot)
                    if (prev_angle >= significant_angle_2):
                        fractures.append(current_plot[-1])
                    current_plot = []

        current_plot.append(data[analize_index])

        prev_angle_delta = angle - prev_angle
        # print(f"Delta: {prev_angle_delta}\n")
        prev_angle = angle

    current_plot += data[len(data) - total_indent_len:]
    plots.append(current_plot)
    fractures.append(data[-1])

    del plots[-1][-(averaging_segment_len + indenting_segment_len):]
    del plots[0][:(averaging_segment_len + indenting_segment_len)]

    array_c = [[plot_id for point in plots[plot_id]] for plot_id in range(len(plots))]

    return plots, array_c, fractures


# Функция перестает работать в случае получение некорректных данных (их отсутствия) хотя бы об одном срезе. В таком случае данные зануляются.

def final_analize_3(data_3d):
    start = datetime.datetime.now()
    try:
        result = {"horizontal": [], "vertical": []}

        first_horizontal_test_slice = get_artificial_data_cut(0.1, data_3d)

        new_cur_lines, array_c, fractures = one_way_analize_advanced(first_horizontal_test_slice,
                                                                     averaging_segment_len=5,
                                                                     indenting_segment_len=10,
                                                                     significant_angle_1=0.6,
                                                                     significant_angle_2=2.1,
                                                                     noise_angle=0.4,
                                                                     not_meaning=7)
        if(len(array_c) < 100):
            return {"horizontal": [[0, 0, 0], [0, 0, 0], [0, 0, 0]], "vertical": [[0, 0, 0], [0, 0, 0], [0, 0, 0]]}

        horizontal_biggest_plot_reference, horizontal_biggest_min_digression_reference, horizontal_biggest_max_digression_reference, horizontal_plot_coordinates_reference = calculate_info(
            first_horizontal_test_slice, fractures)
        display_data(new_cur_lines, array_c, fractures, etalon_marker = True)

        result["horizontal"].append([horizontal_biggest_plot_reference, horizontal_biggest_min_digression_reference,
                                     horizontal_biggest_max_digression_reference])

        left_coordinate_Z = horizontal_plot_coordinates_reference[0][0] + 0.05
        right_coordinate_Z = horizontal_plot_coordinates_reference[-1][0] - 0.05
        center_Z = (horizontal_plot_coordinates_reference[0][0] + horizontal_plot_coordinates_reference[-1][0]) / 2

        print(f'left: {left_coordinate_Z}, right: {left_coordinate_Z}')
        print(f'Center: {center_Z}')

        for i in [left_coordinate_Z, center_Z, left_coordinate_Z]:
            vertical_slice = get_data_cut(i, data_3d)

            new_cur_lines, array_c, fractures = one_way_analize_advanced(vertical_slice,
                                                                         averaging_segment_len=5,
                                                                         indenting_segment_len=10,
                                                                         significant_angle_1=0.6,
                                                                         significant_angle_2=0.9,
                                                                         noise_angle=0.4,
                                                                         not_meaning=5)

            if (len(array_c) < 150):
                return {"horizontal": [[0, 0, 0], [0, 0, 0], [0, 0, 0]], "vertical": [[0, 0, 0], [0, 0, 0], [0, 0, 0]]}

            vertical_biggest_plot, vertical_biggest_min_digression, vertical_biggest_max_digression, vertical_plot_coordinates = calculate_info(
                vertical_slice, fractures)
            display_data(new_cur_lines, array_c, fractures, etalon_marker = True)

            result["vertical"].append(
                [vertical_biggest_plot, vertical_biggest_min_digression, vertical_biggest_max_digression])

        top_horizontal_X = vertical_plot_coordinates[-1][0]
        bottom_horizontal_X = vertical_plot_coordinates[0][0]
        print(f'top_horizontal_X: {top_horizontal_X}, bottom_horizontal_X: {bottom_horizontal_X}')

        for i in [top_horizontal_X, bottom_horizontal_X]:
            horizontal_slice = get_artificial_data_cut(i, data_3d)
            new_cur_lines, array_c, fractures = one_way_analize_advanced(horizontal_slice,
                                                                         averaging_segment_len=5,
                                                                         indenting_segment_len=10,
                                                                         significant_angle_1=0.6,
                                                                         significant_angle_2=2.1,
                                                                         noise_angle=0.4,
                                                                         not_meaning=7)
            if (len(array_c) < 100):
                return {"horizontal": [[0, 0, 0], [0, 0, 0], [0, 0, 0]], "vertical": [[0, 0, 0], [0, 0, 0], [0, 0, 0]]}

            horizontal_biggest_plot, horizontal_biggest_min_digression, horizontal_biggest_max_digression, horizontal_plot_coordinates = calculate_info(
                horizontal_slice, fractures)
            display_data(new_cur_lines, array_c, fractures, etalon_marker = True)

            result["horizontal"].append(
                [horizontal_biggest_plot, horizontal_biggest_min_digression, horizontal_biggest_max_digression])

        end = datetime.datetime.now()
        result_time = end - start
        print(f"\nTime: {result_time}")
    except Exception:
        result = {"horizontal": [[0, 0, 0], [0, 0, 0], [0, 0, 0]], "vertical": [[0, 0, 0], [0, 0, 0], [0, 0, 0]]}
    return result


slices = 1500
seconds_period = 0.013
str0 = []
arrays = []
arrays_csv = []

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('192.168.0.3', 2112))

# str1 = '020202020000144273534E204C4D447363616E646174616D6F6E200001000101328CDB0000A4AA7C3A83C785BC83C789180000080000000000EA60000021C00001000000000000000344495354313DCCCCCD00000000000864700341034900000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000062222E8F2EB02E932E952E982E8D2E972E8E2E992E8E2E832E8E2E832E7F2E872E892E7D2E732E712E6E2E742E762E7F2E682E722E6A2E5A2E512E622E642E642E5A2E632E5E2E4D2E4E2E502E632E502E4C2E452E4C2E552E502E522E4D2E562E402E512E3C2E402E462E402E442E382E482E402E422E332E282E342E3F2E312E3A2E2D2E312E382E382E222E3D2E342E2A2E332E332E282E312E372E3B2E322E342E322E2F2E292E192E362E2C2E232E212E2D2E2D2E2D2E152E3D2E432E282E252E442E2F2E272E2C2E202E152E4B2E202E282E262E3D2E3F2E3B2E382E322E2B2E2F2E3C2E412E2C2E332E362E3D2E3A2E2B2E232E302E402E3D2E562E4C2E432E492E542E4D2E4D2E482E562E682E472E572E3E2E6B2E3D2E622E5F2E612E502E5C2E622E4D2E542E6B2E642E682E612E712E602E642E692E692E6F2E722E7E2E6E2E832E782E7E2E872E782E882E8F2E8E2E822E962E9C2EA12E9D2EAD2E972EA32E982EAE2EAE2EA52EA72EA72EBE2EAC2EBB2EB42EB72ED52EC22EBF2ED32EC62EC62EDD2ED22ECE2EC82EE62EE22EEE2EED2EEE2EE12EE92EF42EE82F022F0B2F002F082F0C2F242F0F2F132F0D2F0A2F0C2E342E122DA72D872D912D9F2D652D6D2D702D4F2D56000100012D732D712D6D2D5F2D7E2D842DA12DA32D842DCA2DEB2E252E012E262DE22DED2D992D7F2DAA2D742D892D8B2D662D832D682D342D562D3B2CFF2CC72C942C742C482C1C2BCE2B9E2B872B062AB52AB329D329D22967290A28A62935298B28D328A728EA27CC27E926D727052708273A26B325E825D0259524CC255023FA247724C9240923DE240F235422E2230922CE21AF23AA213E2161219F214A216A219A211421D9204820D820571FB31FA81ED320191F881F3D1F651FA41F081E511E521DE91EC11E381D431DCA1DB51D511DB81D051D761D211C831C4F1BF41C2C1C991C421BD11C641BB71BE81B3F1B911B671A6E1A931AFC1B371B741B461AC01A341A681A5A1A4A1A811A6919A01A6C0000000000001977000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000005245464C313F8000000000000000086470034103490000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000014001E002B002F003000310033003300340033003400340033003300330035003500360035003600360036003500370036003500350035003500370037003700360037003700390038003A0039003900380038003A00380038003700380039003A003A00380039003A003A003B00390038003800370039003900370037003700390039003900370039003700370038003800370037003800390038003800370038003A003A003A003A0039003800370038003600350029001700150014001400150015001600160015001400130013001D002F003700390039003900370037003500360038003800370037003A0039003600380038003800380038003600340036003400330031002C00270021001B001400120012001300130014001A002E00380038003800390038003A0039003900370038003A00390037003500370037003500340034003600370037003700360038003700340032003000320038003A003A0039003600350034003200340035003600360034003300340035003300330033003400340033003200330034003300330033003100320033003300340034003200320032003100310031003200310030002A00160006000500060009000B000E001500200029003D0061FFFFFFFF0085004B0039003A0045002C0017000E000D000B000900070006000600060007000700080009000A000C000D000D000D000D000C000B000A00090009000700070006000500050004000400040003000300030003000300030003000300030003000200030003000300030003000300030003000200020003000300030003000300020002000200030003000300030002000200020003000300030003000300020003000300030003000300030003000300030003000300030002000300030003000300030003000300030003000400030004000300030004000400030004000400040004000400040004000400040004000400040004000400040004000400040004000500050005000400050004000000000000000500000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000414E474C313F800000C7000000000864700341034983118311831283128312831383138313831483148314831583158315831683168316831783178317831883188318831983198319831A831A831A831B831B831B831C831C831C831D831D824D824D824E824E824E824F824F824F825082508250825182518251825282528252825382538253825482548254825582558255825682568256825782578257825882588258825982598259825A825A825A825B825B825B825C825C825C825D825D825D825E825E825E825F825F825F826082608260826182618261826282628262826382638263826482648264826582658265826682668266826782678267826882688268826982698269826A826A826A826B826B826B826C826C826C826C826D826D826D826E826E826E826F826F827082708270827182718271827282728272827382738273827482748274827582758275827682768276827682778277827782788278827982798279827A827A827A827B827B827B827C827C827C827D827D827D827E827E827E827F827F827F827F828082808280828182818282828282828283828382838284828482848285828582858286828682868287828782878288828882888289828982898289828A828A828B828B828B828B828C828C828D828D828D828E828E828E828F828F828F829082908290829182918291829282928292829282938293829382948294829482958295823E81DF81E181E281E481E681E781E981EA81EC81EE81EF81F181F281F481F681F781F981FA81FC81FD81FF820182038204820682078208820A820C820E820F821182128214821582178218821A821C821D821F822182228224822582278229822A822C822D822F823082328234823582378238823A823B823D823E8240824182438245824682488249824B824C824E824F825182528254825582578188825A825C818D825E8260819182638264819682678269819A826C826D819F81A0827281A381A481A681A781A981AA81AB81AD81AE81B081B181B381B481B581B781B881BA81BB81BC81BE81BF81C081C281C381C581C681C781C981CA81CB81CD81CE81CF81D181D281D381D481D681D781D881DA81DB81DC81DD81DF81E081E181E281E481E581E681E781E981EA81EB81EC81ED81EF81F081F181F281F381F481F581F781F881F981FA81FB81FC81FD81FE8200820182028203820482058206820782088209820A820B820C820D820E820F82108211821282138214821582168217821882198219821A821B821C821D821E821F8220822182218222822382248225822582268227822882298229822A822B822C822C822D822E822F82308231823282328233823482358235823682128212823882388239823A823A823B823B823C823D823D823D823E823E823F823F8240824182418242824282438243824482448245824582458246824782478248824882498249824A824A824B824C824C824D824E824E824F824F82508250824F8250825182518252825282548253825482538254825582558256825782568258825782578258825882578258825982588259825A8257825A825A8259825982598258825982578259825882588258825882598257825782578256825582558256825582558253825382538252825181818180817F817E817E817D817C817C817B8179817981788176817681758174817281718170816F816E816C816B816A816881678165816481628161815F815D815C816F817081708155817181718171817281728172817381738173817481748174817581758175817681768176817781778177817881788178817981798179817A817A817A817B817B817B817C817C817C817D817D817D817E817E817E817F817F817F818081808180818181818181818281828182818381838183818481848184818581858185818681868186818781878187818881888188818981898189818A818A818A818B818B818B818C818C818C818D818D818D818E818E818E818F818F818F819081908190819181918191819281928192819381938193819481948194819581958195819681968196819781978197819881988198819981998199819A819A819A819B819B819B819C819C819C819D819D819D819E819E819E819F819F819F819F81A081A081A181A181A181A281A281A281A381A381A381A481A481A481A581A581A581A681A681A681A781A781A781A881A881A881A981A981A981A981AA81AA81AB81AB81AB81AC81AC81AC81AD81AD81AD81AE81AE81AE81AF81AF81AF81B081B081B081B181B181B181B181B281B281B281B381B381B481B481B481B581B581B581B681B681B681B781B781B781B881B8000000000000000000000000B8'

MESSAGE1 = b'\x02\x02\x02\x02\x00\x00\x00\x17\x73\x4D\x4E\x20\x53\x65\x74\x41\x63\x63\x65\x73\x73\x4D\x6F\x64\x65\x20\x03\xF4\x72\x47\x44\xB3'
MESSAGE_START_MON = b'\x02\x02\x02\x02\x00\x00\x00\x14\x73\x45\x4E\x20\x4C\x4D\x44\x73\x63\x61\x6E\x64\x61\x74\x61\x6D\x6F\x6E\x20\x01\x5F'
MESSAGE_STOP_MON = b'\x02\x02\x02\x02\x00\x00\x00\x14\x73\x45\x4E\x20\x4C\x4D\x44\x73\x63\x61\x6E\x64\x61\x74\x61\x6D\x6F\x6E\x20\x00\x5E'

MESSAGE_START_FAST = b'\x02\x02\x02\x02\x00\x00\x00\x11\x73\x45\x4E\x20\x4C\x4D\x44\x73\x63\x61\x6E\x64\x61\x74\x61\x20\x01\x33'
MESSAGE_STOP_FAST = b'\x02\x02\x02\x02\x00\x00\x00\x11\x73\x45\x4E\x20\x4C\x4D\x44\x73\x63\x61\x6E\x64\x61\x74\x61\x20\x01\x32'

client.sendall(MESSAGE_START_FAST)
in_data = client.recv(29)
print(in_data)

print(datetime.datetime.now())
for _ in range(slices):
    time.sleep(seconds_period)
    in_data = client.recv(5192)
    str0.append(in_data.hex())
client.sendall(MESSAGE_STOP_FAST)
print(datetime.datetime.now())
print(str0[0])

# str0.append(str1)

c_arrays_len = []
c_arrays_refl = []
c_arrays_angle = []

z = 0.0015

for i in range(len(str0)):
    value = str0[i]
    # if i == 10:
    #     print(str0[i])
    value = value[182:10358]
    counting_arrays(value[0:3364], 1)
    value = value[3406:len(value)]
    counting_arrays(value[0:3364], 2)
    value = value[3406:len(value)]
    counting_arrays(value[0:3364], 3)
    counting()
    z = z + 0.0015

frame = pd.DataFrame(list(func_chunk(arrays_csv)))
frame.to_csv('test.csv', index=False)

myFile = open('test.csv', 'w')
with myFile:
    writer = csv.writer(myFile)
    writer.writerows(list(func_chunk(arrays_csv)))

print("Writing complete")
# time.sleep(1)
print(arrays[0])

print(final_analize_3(arrays))

print("ok")
time.sleep(100)
