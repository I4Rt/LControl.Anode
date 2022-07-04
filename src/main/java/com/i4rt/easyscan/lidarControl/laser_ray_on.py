import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('192.168.0.3', 2112))

message_ray_on = b'\x02\x02\x02\x02\x00\x00\x00\x10\x73\x4D\x4E\x20\x4C\x4D\x43\x73\x74\x61\x72\x74\x6D\x65\x61\x73\x68'

#MESSAGE2 = b'\x02\x02\x02\x02\x00\x00\x00\x14\x73\x45\x4E\x20\x4C\x4D\x44\x73\x63\x61\x6E\x64\x61\x74\x61\x6D\x6F\x6E\x20\x01\x5F'

client.sendall(message_ray_on)

#print("щщщщ")
n = 0
while n < 2:
    in_data = client.recv(8192)
    #print("ppppp")
    print("From Server :", in_data)
    print(in_data.hex())

    #print(len(in_data))
    n = n+1

#print(''.join(r'\x{02:x}'.format(ord(c)) for c in in_data))
#client.close()