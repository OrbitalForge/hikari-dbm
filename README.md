# hikari-dbm
[![Build Status](https://travis-ci.org/OrbitalForge/hikari-dbm.svg?branch=master)](https://travis-ci.org/OrbitalForge/hikari-dbm)
[![codebeat badge](https://codebeat.co/badges/3f047966-1c6a-4b0f-bb53-7fa1abe02c29)](https://codebeat.co/projects/github-com-orbitalforge-hikari-dbm)
[![codecov.io badge](https://codecov.io/gh/OrbitalForge/hikari-dbm/master.svg)](https://codecov.io/gh/OrbitalForge/hikari-dbm/master.svg)
hikari-dbm is intended to provide a framework for building scalable applications 
hence Database Management (dbm). The other intent is to provide a light-weight 
mapping framework. ORM refers to this as dynamic mapping but I prefer to call it "I need my data".

## About
The reasoning for developing hikari-dbm is driven from frustration with existing
ORM platforms. They tend to be inflexible when writing applications that can
manipulate their underlying functionality. For example, with a traditional ORM,
if you need to add a field to your application you need to update your database
mapping, correlating classes and then finally attempt to run a database upgrade.

hikari-dbm tries to provide a framework to solve that problem by exposing enough
of the database schema manipulation to the application itself therefore freeing
you to write applications that can extend themselves, or rather write a user
freindly interface for achiveing this.

In addition to schema management is multi-database platform compatability. I do
not intend to provide a fully functional schema management framework for every
database platform that has existed but rather a few select databases. Support
for porting to more databases is greatly appreciated; however, maintance will
be limited to our core set. MariaDB/MySQL, PostgreSQL, Oracle, MSSQL & H2.

## Current State
Please check the latest [![Milestones](https://github.com/OrbitalForge/hikari-dbm/milestones)] for current progress.