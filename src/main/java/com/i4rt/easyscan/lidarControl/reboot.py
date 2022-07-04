import socket

client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
client.connect(('192.168.0.3', 2112))
message_login = b'\x02\x02\x02\x02\x00\x00\x00\x17\x73\x4D\x4E\x20\x53\x65\x74\x41\x63\x63\x65\x73\x73\x4D\x6F\x64\x65\x20\x03\xF4\x72\x47\x44\xB3'
message_reboot = b'\x02\x02\x02\x02\x00\x00\x00\x0D\x73\x4D\x4E\x20\x6D\x53\x43\x72\x65\x62\x6F\x6F\x74\x2C'

client.sendall(message_login)
client.sendall(message_reboot)


n = 0
while n < 2:
    in_data = client.recv(8192)
    print("From Server :", in_data)
    print(in_data.hex())
    n = n+1
