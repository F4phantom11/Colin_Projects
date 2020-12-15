import pandas as pd
import matplotlib
import tkinter as tk
import numpy as np
import matplotlib.pyplot as plt
import mysql.connector as con
import datetime as da
def ActivtyHourChar(conn):
    cursor = conn.cursor()
    cursor.execute("Select * from activity")
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])

    print(Activity)
    print(hours)
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()

def AddToDB(conn):
    cursor = conn.cursor()
    val = ("Horrible", 30,"2","14","2020")
    cursor.execute("Insert into activity(activityName,hours,days,months,years) values(%s,%s,%s,%s,%s)",val)
    conn.commit()
def AddToDB(conn,act,hour,day,month,year):
    cursor = conn.cursor()
    val = (act, float(hour),day,month,year)
    cursor.execute("Insert into activity(activityName,hours,days,months,years) values(%s,%s,%s,%s,%s)",val)
    conn.commit()

def GetText():
    activity = Atext.get()
    hours = Htext.get()
    day = da.datetime.today().day
    month = da.datetime.today().month
    year = da.datetime.today().year
    Atext.selection_clear()
    AddToDB(conn,activity,hours,day,month,year)

def GetTextForDays():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where days = %s",(Daytext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()
def GetTextForMonth():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where months = %s",(Monthtext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()
def GetTextForYear():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where years = %s",(Yeartext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()
def GetTextForMonthYear():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where months = %s and years = %s",(Monthtext.get(),Yeartext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()
def GetTextForDayYear():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where days = %s and years = %s",(Daytext.get(),Yeartext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()
def GetTextForDayMonth():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where days = %s and months = %s",(Daytext.get(),Monthtext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()
def GetTextForDayMonthYear():
    cursor = conn.cursor()
    cursor.execute("Select * from activity where days = %s and months = %s and years = %s",(Daytext.get(),Monthtext.get(),Yeartext.get(),))
    result = cursor.fetchall()
    hours = []
    Activity = []
    for x in result:
        if not Activity.__contains__(x[1]):
            hours.append(int(x[2]))
            Activity.append(x[1])
        else:
            hours[Activity.index(x[1])] += int(x[2])
    x = np.arange(len(Activity))
    axs = plt.bar(x - .35 / 2, hours, .35, align='center', alpha=.5, label="TEst")
    plt.xticks(x, Activity)
    plt.show()

conn = con.connect(host = "localhost",user ="root",passwd ="2Notebook1",database = "videogames",auth_plugin='mysql_native_password')
command = ""
screen = tk.Tk()
screen.geometry("500x300")

graph = tk.Button(screen,text = "Show Graph",fg="green", command =lambda:ActivtyHourChar(conn= conn)).grid(row=0,column= 1)
#graph.place(relx= 0,x=0,y=0,anchor = "nw")

submit = tk.Button(screen,text = "Sumbit",fg="green", command =lambda :GetText()).grid(row= 0)

#submit.place(relx= 0,x=150,y=0,anchor = "nw")

month = tk.Button(screen,text = "Month",fg="green", command =lambda :GetTextForMonth()).grid(row=0,column= 3)
#month.place(relx= 0,x=200,y=0,anchor = "nw")

year = tk.Button(screen,text = "Year",fg="green", command =lambda :GetTextForYear()).grid(row=0,column= 4)
#year.place(relx= 0,x=250,y=0,anchor = "nw")

day = tk.Button(screen,text = "Day",fg="green", command =lambda :GetTextForDays()).grid(row=0,column= 2)
#day.place(relx= 0,x=300,y=0,anchor = "nw")

MonthYear = tk.Button(screen,text = "MY",fg="green", command =lambda :GetTextForMonthYear()).grid(row=0,column= 5)
#MonthYear.place(relx= 0,x=350,y=0,anchor = "nw")

DayYear = tk.Button(screen,text = "DY",fg="green", command =lambda :GetTextForDayYear()).grid(row=0,column= 6)
#DayYear.place(relx= 0,x=400,y=0,anchor = "nw")

DayMonth = tk.Button(screen,text = "DM",fg="green", command =lambda :GetTextForDayMonth()).grid(row=0,column= 7)
#DayMonth.place(relx= 0,x=450,y=0,anchor = "nw")

DayMonthYear = tk.Button(screen,text = "DMY",fg="green", command =lambda :GetTextForDayMonthYear()).grid(row=0,column= 8)

tk.Button(screen,text = "Quit",fg="green", command =lambda :quit()).grid(row=0,column= 9)
#DayMonthYear.place(relx= 0,x=470,y=0,anchor = "nw")
tk.Label(screen, text="Activity:").grid(row=2)
tk.Label(screen, text="Hours:").grid(row=3)
tk.Label(screen, text="Day:").grid(row=4)
tk.Label(screen, text="Month:").grid(row=5)
tk.Label(screen, text="Year:").grid(row=6)
Atext = tk.Entry(screen)
Atext.grid(row=2,column= 1)

#Atext.place(relx=0,x=40,y=30,anchor = "nw")

Htext = tk.Entry(screen)
Htext.grid(row=3,column= 1)

Daytext = tk.Entry(screen)
Daytext.grid(row=4,column= 1)


Monthtext = tk.Entry(screen)
Monthtext.grid(row=5,column= 1)


Yeartext = tk.Entry(screen)
Yeartext.grid(row=6,column= 1)

#graph.pack(side = tk.LEFT)
screen.mainloop()